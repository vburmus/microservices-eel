package com.epam.esm.utils.openfeign;

public record CreateUserRequest(
        String name,
        String surname,
        String email,
        String phone) {
}