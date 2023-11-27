package com.epam.esm.certificate.controllers;


import com.epam.esm.certificate.models.CertificateDTO;
import com.epam.esm.certificate.service.CertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/v1/certificates")
@RequiredArgsConstructor
public class CertificateController {
    public final CertificateService giftCertificateService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificateDTO> create(@Valid @RequestPart("certificate") CertificateDTO certificateDTO,
                                                 @RequestPart("image") Optional<MultipartFile> image) {
        return ResponseEntity.ok(giftCertificateService.create(certificateDTO, image));
    }

    @GetMapping
    public ResponseEntity<Page<CertificateDTO>> readAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(giftCertificateService.readAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateDTO> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(giftCertificateService.getById(id));
    }

    @GetMapping({"/search/by-tags"})
    public ResponseEntity<Page<CertificateDTO>> getBySeveralTags(@RequestParam("tagIds") List<Long> tagsId,
                                                                 @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(giftCertificateService.getBySeveralTags(tagsId, pageable));
    }

    @GetMapping("/search/by-name-or-description-part")
    public ResponseEntity<Page<CertificateDTO>> getByNameOrDescriptionPart(@RequestParam("part") String nameOrDescriptionPart,
                                                                           @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(giftCertificateService.getByNameOrShortDescriptionPart(nameOrDescriptionPart,
                pageable));
    }

    @GetMapping({"/search/by-tags-and-part"})
    public ResponseEntity<Page<CertificateDTO>> getByTagsAndShortDescription(@RequestParam("tagIds") List<Long> tagIds,
                                                                             @RequestParam("part") String part,
                                                                             @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(giftCertificateService.getByTagsAndShortDescriptionOrNamePart(tagIds,
                part, pageable));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CertificateDTO> updateCertificate(
            @PathVariable("id") long id,
            @RequestPart(value = "patch") JsonMergePatch patch,
            @RequestPart(value = "image") Optional<MultipartFile> image) throws JsonPatchException,
            JsonProcessingException {
        return ResponseEntity.ok(giftCertificateService.update(id, patch, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}