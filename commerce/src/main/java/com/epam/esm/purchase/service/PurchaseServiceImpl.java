package com.epam.esm.purchase.service;

import com.epam.esm.certificate.models.Certificate;
import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.model.AuthenticatedUser;
import com.epam.esm.purchase.models.Purchase;
import com.epam.esm.purchase.models.PurchaseCreationRequest;
import com.epam.esm.purchase.models.PurchaseDTO;
import com.epam.esm.purchase.models.PurchasePosition;
import com.epam.esm.purchase.repository.PurchaseRepository;
import com.epam.esm.purchasecertificate.model.PurchaseCertificate;
import com.epam.esm.purchasecertificate.model.PurchaseCertificatePK;
import com.epam.esm.purchasecertificate.repository.PurchaseCertificateRepository;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.amqp.MessagePublisher;
import com.epam.esm.utils.amqp.PurchaseCreationMessage;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.epam.esm.model.Role.ADMIN;
import static com.epam.esm.utils.Constants.PURCHASE_DOES_NOT_EXISTS_ID;

@Service
@RequiredArgsConstructor
public class PurchaseServiceImpl implements PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseCertificateRepository purchaseCertificateRepository;
    private final CertificateService certificateService;
    private final EntityToDtoMapper entityToDtoMapper;
    private final MessagePublisher messagePublisher;

    @Transactional
    public PurchaseDTO create(PurchaseCreationRequest purchaseCreationRequest) {
        Map<Certificate, Integer> certificateQuantityMap = createCertificateQuantityMap(purchaseCreationRequest);
        AuthenticatedUser user =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Purchase savedPurchase = purchaseRepository.save(
                Purchase.builder()
                        .userId(user.getId())
                        .description(purchaseCreationRequest.description())
                        .cost(getPurchaseCost(certificateQuantityMap))
                        .build());
        Set<PurchaseCertificate> purchaseCertificates = createPurchaseCertificateSet(certificateQuantityMap,
                savedPurchase);
        savedPurchase.setPurchaseCertificates(purchaseCertificates);

        PurchaseCreationMessage message = PurchaseCreationMessage.builder()
                .email(user.getEmail())
                .purchaseDTO(entityToDtoMapper.toPurchaseDTO(savedPurchase))
                .build();
        messagePublisher.publishPurchaseCreationMessage(message);
        return entityToDtoMapper.toPurchaseDTO(savedPurchase);
    }

    public Page<PurchaseDTO> readAll(Pageable pageable) {
        return purchaseRepository.findAll(pageable).map(entityToDtoMapper::toPurchaseDTO);
    }

    public PurchaseDTO getById(long id) {
        return purchaseRepository.findById(id).map(entityToDtoMapper::toPurchaseDTO)
                .orElseThrow(() -> new NoSuchObjectException(String.format(PURCHASE_DOES_NOT_EXISTS_ID, id)));
    }

    public Page<PurchaseDTO> getAllByUserId(long userId, Pageable pageable) {
        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(authenticatedUser.getId() == userId || authenticatedUser.getRole().equals(ADMIN))) {
            throw new AccessDeniedException("ACCESS_DENIED");
        }
        return purchaseRepository.findAllByUserId(userId, pageable).map(entityToDtoMapper::toPurchaseDTO);
    }

    private Set<PurchaseCertificate> createPurchaseCertificateSet(Map<Certificate, Integer> certificateQuantityMap,
                                                                  Purchase savedPurchase) {
        return certificateQuantityMap.entrySet().stream().map(entry -> {
            Certificate certificate = entry.getKey();
            Integer quantity = entry.getValue();
            PurchaseCertificatePK purchaseCertificatePK = new PurchaseCertificatePK(savedPurchase.getId(),
                    certificate.getId());
            return purchaseCertificateRepository.save(
                    new PurchaseCertificate(purchaseCertificatePK, certificate, savedPurchase, quantity));
        }).collect(Collectors.toSet());
    }

    private Map<Certificate, Integer> createCertificateQuantityMap(PurchaseCreationRequest purchaseCreationRequest) {
        Map<Certificate, Integer> certificateQuantityMap = new HashMap<>();
        for (PurchasePosition position : purchaseCreationRequest.positions()) {
            Certificate certificate =
                    entityToDtoMapper.toCertificate(certificateService.getById(position.certificateId()));
            certificateQuantityMap.put(certificate, position.quantity());
        }
        return certificateQuantityMap;
    }

    private BigDecimal getPurchaseCost(Map<Certificate, Integer> certificateQuantityMap) {
        BigDecimal totalCost = BigDecimal.ZERO;
        for (Map.Entry<Certificate, Integer> entry : certificateQuantityMap.entrySet()) {
            Certificate certificate = entry.getKey();
            Integer quantity = entry.getValue();
            BigDecimal certificateCost = certificate.getPrice();
            BigDecimal lineItemCost = certificateCost.multiply(BigDecimal.valueOf(quantity));
            totalCost = totalCost.add(lineItemCost);
        }
        return totalCost;
    }
}