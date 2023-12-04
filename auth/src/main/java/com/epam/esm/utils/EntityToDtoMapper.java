package com.epam.esm.utils;

import com.epam.esm.utils.amqp.CreateUserRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.model.AuthenticatedUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    @Mapping(target = "id", ignore = true)
    CreateUserRequest toUserCreationRequest(RegisterRequest registerRequest);
    @Mapping(source = "username", target = "email")
    AuthenticatedUser toAuthenticatedUser(Credentials credentials);
}