package com.epam.esm.controllers;

import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.UserResponse;
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

@RestController
@RequestMapping(value = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> create(@Valid @RequestPart("user") RegisterRequest registerRequest,
                                               @RequestPart MultipartFile image) {
        UserResponse createdUser = userService.create(registerRequest, image);
        return ResponseEntity.created(URI.create("/api/v1/users/" + createdUser.id())).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> readAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.readAll(pageable));
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponse> getByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getByEmail(email));
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> update(
            @PathVariable("id") long id,
            @RequestPart(value = "patch") JsonMergePatch patch,
            @RequestPart(value = "image", required = false) MultipartFile image) throws JsonPatchException,
            JsonProcessingException {
        return ResponseEntity.ok(userService.update(id, patch, image));
    }
}