package com.epam.esm.certificate.service;

import com.epam.esm.certificate.models.Certificate;
import com.epam.esm.certificate.models.CertificateDTO;
import com.epam.esm.certificate.repository.CertificateRepository;
import com.epam.esm.tag.models.Tag;
import com.epam.esm.tag.service.TagService;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.Validation;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchObjectException;
import com.epam.esm.utils.exceptionhandler.exceptions.ObjectAlreadyExists;
import com.epam.esm.utils.exceptionhandler.exceptions.ObjectInvalidException;
import com.epam.esm.utils.openfeign.AwsUtilsFeignClient;
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
import java.util.Optional;

import static com.epam.esm.utils.Constants.TAGS;
import static com.epam.esm.utils.Constants.TAG_DOESNT_EXIST_ID;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Service
@RequiredArgsConstructor
public class CertificateService {
    public static final String UPDATE_CERTIFICATE_IS_NULL = "Error updating certificate. Updated certificate is null.";
    private final CertificateRepository certificateRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final AwsUtilsFeignClient awsClient;
    private final TagService tagService;
    private final ObjectMapper objectMapper;
    @Value("${certificate.default.image.url}")
    private String defaultImageUrl;

    @Transactional
    public CertificateDTO create(CertificateDTO giftCertificateDTO, Optional<MultipartFile> image) {
        Certificate certificate = entityToDtoMapper.toCertificate(giftCertificateDTO);
        ExampleMatcher gcMatcher = ExampleMatcher.matching()
                .withIgnorePaths(CREATE_DATE, LAST_UPDATE, ID, PRICE)
                .withMatcher(NAME, exact())
                .withMatcher(SHORT_DESCRIPTION, exact())
                .withMatcher(DURATION_DATE, exact());
        Example<Certificate> providedGC = Example.of(certificate, gcMatcher);
        List<Tag> tags = certificate.getTags();

        if (tags == null) throw new NullableTagsException(CERTIFICATE_SHOULD_HAVE_AT_LEAST_ONE_TAG);
        if (certificateRepository.exists(providedGC)) throw new ObjectAlreadyExists(
                String.format(CERTIFICATE_EXISTS, certificate.getName(), certificate.getDurationDate()));
        if (!Validation.isValidCertificate(certificate)) throw new ObjectInvalidException(
                String.format(CERTIFICATE_IS_INVALID, certificate.getName(), certificate.getDurationDate()));

        certificate.setCreateDate(LocalDateTime.now());
        certificate.setLastUpdateDate(LocalDateTime.now());
        certificate.setTags(tagService.checkTagsAndFetch(tags));
        image.ifPresentOrElse(
                img -> certificate.setImageUrl(awsClient.uploadImage(CERTIFICATES, img)),
                () -> certificate.setImageUrl(defaultImageUrl)
        );
        return entityToDtoMapper.toCertificateDTO(certificateRepository.save(certificate));
    }

    public Page<CertificateDTO> readAll(Pageable pageable) {
        Page<Certificate> allGCs = certificateRepository.findAll(pageable);
        return allGCs.map(entityToDtoMapper::toCertificateDTO);
    }

    public CertificateDTO getById(long id) {
        return certificateRepository.findById(id)
                .map(entityToDtoMapper::toCertificateDTO)
                .orElseThrow(() -> new NoSuchObjectException(String.format(CERTIFICATE_DOES_NOT_EXISTS_ID, id)));
    }

    public Page<CertificateDTO> getBySeveralTags(List<Long> tagsId, Pageable pageable) {
        return certificateRepository.findByTagsIdIn(tagsId, pageable)
                .map(entityToDtoMapper::toCertificateDTO);
    }

    public Page<CertificateDTO> getByTagsAndShortDescriptionOrNamePart(List<Long> tagsId,
                                                                       String part,
                                                                       Pageable pageable) {
        return certificateRepository.findByTagsIdInAndShortDescriptionOrNameContaining(tagsId,
                part, pageable).map(entityToDtoMapper::toCertificateDTO);
    }

    public Page<CertificateDTO> getByNameOrShortDescriptionPart(String part, Pageable pageable) {
        return certificateRepository.findByPartialNameOrDescription(part, pageable)
                .map(entityToDtoMapper::toCertificateDTO);
    }

    @Transactional
    public CertificateDTO updateCertificate(long id, JsonMergePatch jsonPatch, Optional<MultipartFile> image) throws JsonPatchException,
            JsonProcessingException {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(() -> new NoSuchObjectException(String.format(CERTIFICATE_DOES_NOT_EXISTS_ID, id)));
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(certificate, JsonNode.class));
        Certificate updatedCertificate = objectMapper.treeToValue(patched, Certificate.class);
        if (updatedCertificate == null) throw new CertificateUpdateException(UPDATE_CERTIFICATE_IS_NULL);
        mapUpdatedFields(certificate, updatedCertificate);
        image.ifPresent(img -> certificate.setImageUrl(awsClient.uploadImage(TAGS, img)));
        return entityToDtoMapper.toCertificateDTO(certificateRepository.save(certificate));
    }

    private void mapUpdatedFields(Certificate certificate, Certificate updatedCertificate) {
        certificate.setPrice(updatedCertificate.getPrice());
        certificate.setName(updatedCertificate.getName());
        certificate.setDurationDate(updatedCertificate.getDurationDate());
        certificate.setTags(tagService.checkTagsAndFetch(updatedCertificate.getTags()));
        certificate.setShortDescription(updatedCertificate.getShortDescription());
        certificate.setLongDescription(updatedCertificate.getLongDescription());
    }

    public void delete(Long id) {
        certificateRepository.findById(id).ifPresentOrElse(
                certificate -> certificateRepository.deleteById(id),
                () -> {
                    throw new NoSuchObjectException(String.format(TAG_DOESNT_EXIST_ID, id));
                }
        );
    }
}