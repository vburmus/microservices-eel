package com.epam.esm.controllers;

import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.UserDTO;
import com.epam.esm.service.UserService;
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

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> create(@Valid @RequestPart("user") RegisterRequest registerRequest,
                                          @RequestPart("image") Optional<MultipartFile> image) {
        UserDTO createdUser = userService.create(registerRequest, image);
        return ResponseEntity.created(URI.create("/api/v1/users/" + createdUser.id())).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> readAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.readAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDTO> update(
            @PathVariable("id") long id,
            @RequestPart(value = "patch") JsonMergePatch patch,
            @RequestPart(value = "image") Optional<MultipartFile> image) throws JsonPatchException,
            JsonProcessingException {
        return ResponseEntity.ok(userService.update(id, patch, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}