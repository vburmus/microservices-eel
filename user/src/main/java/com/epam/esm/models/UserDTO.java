package com.epam.esm.models;

public record UserDTO(
        Long id,
        String name,
        String surname,
        String phone,
        String email,
        String imageUrl) {
}