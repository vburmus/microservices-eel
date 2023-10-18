package com.epam.esm.auth.models;

public record CreateUserRequest(
        String name,
        String surname,
        String email,
        String phone) {
}