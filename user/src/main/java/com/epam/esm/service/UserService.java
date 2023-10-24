package com.epam.esm.service;

import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.UserDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface UserService {
    UserDTO create(RegisterRequest registerRequest, Optional<MultipartFile> image);

    Page<UserDTO> readAll(Pageable pageable);

    UserDTO getByEmail(String email);

    UserDTO update(Long id, JsonMergePatch jsonPatch, Optional<MultipartFile> image) throws JsonPatchException,
            JsonProcessingException;

    void delete(Long id);
}