package com.epam.esm.utils;

import com.epam.esm.models.User;
import com.epam.esm.models.UserResponse;
import com.epam.esm.utils.ampq.CreateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    @Mapping(target = "imageUrl", ignore = true)
    User toUser(CreateUserRequest createUserRequest);

    UserResponse toUserResponse(User user);
}