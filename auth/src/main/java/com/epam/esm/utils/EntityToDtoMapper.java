package com.epam.esm.utils;

import com.epam.esm.utils.openfeign.CreateUserRequest;
import com.epam.esm.auth.models.RegisterRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    CreateUserRequest toUserCreationRequest(RegisterRequest registerRequest);
}