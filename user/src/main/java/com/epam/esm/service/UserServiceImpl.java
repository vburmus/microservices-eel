package com.epam.esm.service;

import com.epam.esm.model.Role;
import com.epam.esm.model.UserDTO;
import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.User;
import com.epam.esm.models.UserResponse;
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
    public UserResponse create(RegisterRequest registerRequest, MultipartFile image) {
        userRepository.findByEmail(registerRequest.email()).ifPresent(user -> {
            throw new UserAlreadyExistException(String.format(ALREADY_REGISTERED, registerRequest.email()));
        });
        User user = entityToDtoMapper.toUser(registerRequest);
        user.setImageUrl(image == null ? defaultImageUrl : awsClient.uploadImage(USERS, image));
        return entityToDtoMapper.toUserResponse(userRepository.save(user));
    }

    public Page<UserResponse> readAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(entityToDtoMapper::toUserResponse);
    }

    public UserResponse getByEmail(String email) {
        return userRepository.findByEmail(email).map(entityToDtoMapper::toUserResponse)
                .orElseThrow(() -> new NoSuchUserException(String.format(USER_DOESNT_EXIST_EMAIL, email)));
    }

    @Transactional
    public UserResponse update(Long id, JsonMergePatch jsonPatch, MultipartFile image) throws JsonPatchException,
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
        if (image != null) user.setImageUrl(awsClient.uploadImage(USERS, image));
        return entityToDtoMapper.toUserResponse(userRepository.save(user));

    }

    public void delete(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> userRepository.deleteById(user.getId()),
                () -> {
                    throw new NoSuchUserException(String.format(USER_DOESNT_EXIST_EMAIL, email));
                }
        );
    }

    private boolean checkIfOperationRestricted(Long id) {
        UserDTO authenticatedUser = (UserDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return !authenticatedUser.getId().equals(id) && !authenticatedUser.getRole().equals(Role.ADMIN);
    }

    private void mapUpdatedFields(User user, User updatedUser) {
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        user.setPhone(updatedUser.getPhone());
    }
}