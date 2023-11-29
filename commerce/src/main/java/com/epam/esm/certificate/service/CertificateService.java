package com.epam.esm.certificate.service;

import com.epam.esm.certificate.models.CertificateDTO;
import com.epam.esm.utils.amqp.ImageUploadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CertificateService {
    CertificateDTO create(CertificateDTO giftCertificateDTO, MultipartFile image);

    Page<CertificateDTO> readAll(Pageable pageable);

    CertificateDTO getById(long id);

    Page<CertificateDTO> getBySeveralTags(List<Long> tagsId, Pageable pageable);

    Page<CertificateDTO> getByTagsAndShortDescriptionOrNamePart(List<Long> tagIds,
                                                                String part,
                                                                Pageable pageable);

    Page<CertificateDTO> getByNameOrShortDescriptionPart(String part, Pageable pageable);

    CertificateDTO update(long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException;

    void delete(Long id);

    void setUploadedImage(ImageUploadResponse response);
}