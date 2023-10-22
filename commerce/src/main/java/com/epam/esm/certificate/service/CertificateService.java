package com.epam.esm.certificate.service;

import com.epam.esm.certificate.models.CertificateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface CertificateService {
    CertificateDTO create(CertificateDTO giftCertificateDTO, Optional<MultipartFile> image);

    Page<CertificateDTO> readAll(Pageable pageable);

    CertificateDTO getById(long id);

    Page<CertificateDTO> getBySeveralTags(List<Long> tagsId, Pageable pageable);

    Page<CertificateDTO> getByTagsAndShortDescriptionOrNamePart(List<Long> tagsId,
                                                                String part,
                                                                Pageable pageable);

    Page<CertificateDTO> getByNameOrShortDescriptionPart(String part, Pageable pageable);

    CertificateDTO updateCertificate(long id, JsonMergePatch jsonPatch, Optional<MultipartFile> image) throws JsonPatchException,
            JsonProcessingException;

    void delete(Long id);
}