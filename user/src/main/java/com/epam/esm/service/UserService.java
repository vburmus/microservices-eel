package com.epam.esm.service;

import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.Role;
import com.epam.esm.models.User;
import com.epam.esm.models.UserDTO;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.exceptionhandler.exceptions.NoSuchUserException;
import com.epam.esm.utils.exceptionhandler.exceptions.UserAlreadyExistException;
import com.epam.esm.utils.exceptionhandler.exceptions.UserUpdateException;
import com.epam.esm.utils.openfeign.AwsUtilsFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final AwsUtilsFeignClient awsClient;
    private final ObjectMapper objectMapper;
    @Value("${user.default.image.url}")
    private String defaultImageUrl;

    @Transactional
    public UserDTO create(RegisterRequest registerRequest, Optional<MultipartFile> image) {
        userRepository.findByEmail(registerRequest.email()).ifPresent(user -> {
            throw new UserAlreadyExistException(String.format(ALREADY_REGISTERED, registerRequest.email()));
        });
        User user = entityToDtoMapper.toUser(registerRequest);
        user.setRole(Role.USER);
        image.ifPresentOrElse(
                img -> user.setImageUrl(awsClient.uploadImage(USERS, img)),
                () -> user.setImageUrl(defaultImageUrl)
        );
        return entityToDtoMapper.toUserDTO(userRepository.save(user));
    }

    public Page<UserDTO> readAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(entityToDtoMapper::toUserDTO);
    }

    public UserDTO getById(Long id) {
        return userRepository.findById(id).map(entityToDtoMapper::toUserDTO)
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_ID, id)));
    }

    @Transactional
    public UserDTO update(Long id, JsonMergePatch jsonPatch, Optional<MultipartFile> image) throws JsonPatchException,
            JsonProcessingException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_ID, id)));
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(user, JsonNode.class));
        User updatedUser = objectMapper.treeToValue(patched, User.class);
        if (updatedUser == null) throw new UserUpdateException(UPDATE_USER_IS_NULL);
        mapUpdatedFields(user, updatedUser);
        image.ifPresent(img -> user.setImageUrl(awsClient.uploadImage(USERS, img)));
        return entityToDtoMapper.toUserDTO(userRepository.save(user));
    }

    private void mapUpdatedFields(User user, User updatedUser) {
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
    }

    public void delete(Long id) {
        userRepository.findById(id).ifPresentOrElse(
                certificate -> userRepository.deleteById(id),
                () -> {
                    throw new NoSuchUserException(String.format(USER_DOESNT_EXIST_ID, id));
                }
        );
    }
}