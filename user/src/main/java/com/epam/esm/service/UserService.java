package com.epam.esm.service;

import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.UserResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    UserResponse create(RegisterRequest registerRequest, MultipartFile image);

    Page<UserResponse> readAll(Pageable pageable);

    UserResponse getByEmail(String email);

    UserResponse update(Long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException;

    void delete(String email);
}