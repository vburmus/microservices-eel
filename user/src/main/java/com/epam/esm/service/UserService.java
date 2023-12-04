package com.epam.esm.service;

import com.epam.esm.models.UserDTO;
import com.epam.esm.utils.ampq.CreateUserRequest;
import com.epam.esm.utils.ampq.ImageUploadResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {
    void create(CreateUserRequest registerRequest);
    Page<UserDTO> readAll(Pageable pageable);

    UserDTO getByEmail(String email);

    UserDTO getById(Long id);

    UserDTO update(Long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException;

    void delete(String email);

    void setUploadedImage(ImageUploadResponse imageUploadResponse);
}