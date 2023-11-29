package com.epam.esm.certificate.service;

import com.epam.esm.certificate.models.Certificate;
import com.epam.esm.certificate.models.CertificateDTO;
import com.epam.esm.certificate.repository.CertificateRepository;
import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.amqp.ImageUploadResponse;
import com.epam.esm.utils.amqp.MessagePublisher;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchObjectException;
import com.epam.esm.utils.exceptionhandler.exceptions.ObjectAlreadyExists;
import com.epam.esm.utils.exceptionhandler.exceptions.UpdateException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.utils.Constants.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
public class CertificateServiceImpl implements CertificateService {
    private final CertificateRepository certificateRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final MessagePublisher messagePublisher;
    private final TagService tagService;
    private final ObjectMapper objectMapper;
    @Value("${certificate.default.image.url}")
    private String defaultImageUrl;
    @Value("${certificate.image.exchange}")
    private String certificateImageExchange;
    @Value("${certificate.image.key}")
    private String certificateImageRoutingKey;

    @Transactional
    @Override
    public CertificateDTO create(CertificateDTO giftCertificateDTO, MultipartFile image) {
        Certificate certificate = entityToDtoMapper.toCertificate(giftCertificateDTO);
        if (ifExist(certificate))
            throw new ObjectAlreadyExists(String.format(CERTIFICATE_EXISTS, certificate.getName(),
                    certificate.getDurationDate()));

        LocalDateTime now = LocalDateTime.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);

        List<Tag> tags = certificate.getTags();
        certificate.setTags(tagService.checkTagsAndFetch(tags));
        certificate.setImageUrl(defaultImageUrl);
        Certificate savedCertificate = certificateRepository.save(certificate);
        if (image != null) {
            messagePublisher.publishImageUploadMessage(image, savedCertificate.getId(), certificateImageExchange,
                    certificateImageRoutingKey);
        }
        return entityToDtoMapper.toCertificateDTO(savedCertificate);
    }

    @Override
    public Page<CertificateDTO> readAll(Pageable pageable) {
        Page<Certificate> allGCs = certificateRepository.findAll(pageable);
        return allGCs.map(entityToDtoMapper::toCertificateDTO);
    }

    @Override
    public CertificateDTO getById(long id) {
        return certificateRepository.findById(id)
                .map(entityToDtoMapper::toCertificateDTO)
                .orElseThrow(() -> new NoSuchObjectException(String.format(CERTIFICATE_DOES_NOT_EXISTS_ID, id)));
    }

    @Override
    public Page<CertificateDTO> getBySeveralTags(List<Long> tagsId, Pageable pageable) {
        return certificateRepository.findByTagsIdIn(tagsId, pageable)
                .map(entityToDtoMapper::toCertificateDTO);
    }

    @Override
    public Page<CertificateDTO> getByTagsAndShortDescriptionOrNamePart(List<Long> tagIds,
                                                                       String part,
                                                                       Pageable pageable) {
        return certificateRepository.findByTagsIdInAndShortDescriptionOrNameContaining(tagIds,
                part, pageable).map(entityToDtoMapper::toCertificateDTO);
    }

    @Override
    public Page<CertificateDTO> getByNameOrShortDescriptionPart(String part, Pageable pageable) {
        return certificateRepository.findByPartialNameOrDescription(part, pageable)
                .map(entityToDtoMapper::toCertificateDTO);
    }

    @Transactional
    @Override
    public CertificateDTO update(long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new NoSuchObjectException(String.format(CERTIFICATE_DOES_NOT_EXISTS_ID, id)));
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(certificate, JsonNode.class));
        Certificate updatedCertificate = objectMapper.treeToValue(patched, Certificate.class);
        if (updatedCertificate == null) throw new UpdateException(UPDATE_CERTIFICATE_IS_NULL);
        mapUpdatedFields(certificate, updatedCertificate);
        if (image != null) {
            messagePublisher.publishImageUploadMessage(image, id, certificateImageExchange,
                    certificateImageRoutingKey);
        }
        return entityToDtoMapper.toCertificateDTO(certificateRepository.save(certificate));
    }

    @Override
    public void delete(Long id) {
        certificateRepository.findById(id).ifPresentOrElse(
                certificate -> certificateRepository.deleteById(id),
                () -> {
                    throw new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_ID, id));
                }
        );
    }



    private void mapUpdatedFields(Certificate certificate, Certificate updatedCertificate) {
        certificate.setPrice(updatedCertificate.getPrice());
        certificate.setName(updatedCertificate.getName());
        certificate.setDurationDate(updatedCertificate.getDurationDate());
        certificate.setTags(tagService.checkTagsAndFetch(updatedCertificate.getTags()));
        certificate.setShortDescription(updatedCertificate.getShortDescription());
        certificate.setLongDescription(updatedCertificate.getLongDescription());
    }

    @Override
    public void setUploadedImage(ImageUploadResponse response) {
        Certificate certificate = certificateRepository.findById(response.id())
                .orElseThrow(() -> new NoSuchObjectException(String.format(CERTIFICATE_DOES_NOT_EXISTS_ID,
                        response.id())));
        certificate.setImageUrl(response.imageUrl());
        certificateRepository.save(certificate);
    }

    private boolean ifExist(Certificate certificate) {
        ExampleMatcher gcMatcher = ExampleMatcher.matching()
                .withIgnorePaths(CREATE_DATE, LAST_UPDATE, ID, PRICE)
                .withMatcher(NAME, exact())
                .withMatcher(SHORT_DESCRIPTION, exact())
                .withMatcher(DURATION_DATE, exact());
        return certificateRepository.exists(Example.of(certificate, gcMatcher));
    }
}