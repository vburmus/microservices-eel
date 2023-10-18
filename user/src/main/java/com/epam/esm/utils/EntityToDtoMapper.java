package com.epam.esm.utils;

import com.epam.esm.models.RegisterRequest;
import com.epam.esm.models.User;
import com.epam.esm.models.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    User toUser(RegisterRequest registerRequest);

    UserDTO toUserDTO(User userDTO);
}