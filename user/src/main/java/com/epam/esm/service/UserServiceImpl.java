package com.epam.esm.service;

import com.epam.esm.model.AuthenticatedUser;
import com.epam.esm.model.Role;
import com.epam.esm.models.User;
import com.epam.esm.models.UserDTO;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.ampq.CreateUserRequest;
import com.epam.esm.utils.ampq.ImageUploadResponse;
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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EntityToDtoMapper entityToDtoMapper;
    private final AwsUtilsFeignClient awsClient;
    private final ObjectMapper objectMapper;
    @Value("${user.default.image.url}")
    private String defaultImageUrl;

    @Transactional
    @Override
    public void create(CreateUserRequest registerRequest) {
        userRepository.findByEmail(registerRequest.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistException(String.format(ALREADY_REGISTERED, registerRequest.getEmail()));
        });
        User user = entityToDtoMapper.toUser(registerRequest);
        user.setImageUrl(defaultImageUrl);
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> readAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(entityToDtoMapper::toUserDTO);
    }

    @Override
    public UserDTO getByEmail(String email) {
        return userRepository.findByEmail(email).map(entityToDtoMapper::toUserDTO)
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_EMAIL, email)));
    }

    @Override
    public UserDTO getById(Long id) {
        return userRepository.findById(id).map(entityToDtoMapper::toUserDTO)
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_ID, id)));
    }

    @Transactional
    public UserDTO update(Long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
            JsonProcessingException {
        if (checkIfOperationRestricted(id)) {
            throw new AccessDeniedException(OPERATION_NOT_ALLOWED);
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_ID, id)));
        JsonNode patched = jsonPatch.apply(objectMapper.convertValue(user, JsonNode.class));
        User updatedUser = objectMapper.treeToValue(patched, User.class);
        if (updatedUser == null) throw new UserUpdateException(UPDATE_USER_IS_NULL);
        mapUpdatedFields(user, updatedUser);
        return entityToDtoMapper.toUserDTO(userRepository.save(user));
    }

    @Override
    public void delete(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> userRepository.deleteById(user.getId()),
                () -> {
                    throw new NoSuchUserException(String.format(USER_DOESNT_EXIST_EMAIL, email));
                }
        );
    }

    @Override
    public void setUploadedImage(ImageUploadResponse imageUploadResponse) {
        User user = userRepository.findById(imageUploadResponse.id())
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_ID,
                        imageUploadResponse.id())));
        user.setImageUrl(imageUploadResponse.imageUrl());
        userRepository.save(user);
    }

    private boolean checkIfOperationRestricted(Long id) {
        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return !authenticatedUser.getId().equals(id) && !authenticatedUser.getRole().equals(Role.ADMIN);
    }

    private void mapUpdatedFields(User user, User updatedUser) {
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
    }
}