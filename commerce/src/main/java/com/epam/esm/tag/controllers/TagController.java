package com.epam.esm.tag.controllers;

import com.epam.esm.tag.models.TagDTO;
import com.epam.esm.tag.service.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TagDTO> create(@Valid @RequestPart("tag") TagDTO tagDTO,
                                         @RequestPart(value = "image", required = false) MultipartFile image) {
        TagDTO createdTag = tagService.create(tagDTO, image);
        return ResponseEntity.created(URI.create("/api/v1/tags/" + createdTag.id())).body(createdTag);
    }

    @GetMapping
    public ResponseEntity<Page<TagDTO>> readAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(tagService.readAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(tagService.getById(id));
    }

    @GetMapping("/search/by-name-part")
    public ResponseEntity<Page<TagDTO>> getByNamePart(@RequestParam("part") String namePart,
                                                      @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(tagService.getByNamePart(namePart, pageable));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TagDTO> update(
            @PathVariable("id") long id,
            @RequestPart(value = "patch") JsonMergePatch patch,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonPatchException,
            JsonProcessingException {
        return ResponseEntity.ok(tagService.update(id, patch, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}