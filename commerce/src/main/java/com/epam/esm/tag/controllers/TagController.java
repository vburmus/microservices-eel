package com.epam.esm.tag.controllers;

import com.epam.esm.tag.models.TagDTO;
import com.epam.esm.tag.service.TagService;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TagDTO> create(@Valid @RequestPart("tag") TagDTO tagDTO,
                                         @RequestParam Optional<MultipartFile> image) {
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/by-name-part")
    public ResponseEntity<Page<TagDTO>> getByNamePart(@RequestParam("part") String namePart,
                                                      @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(tagService.getByNamePart(namePart, pageable));
    }
}