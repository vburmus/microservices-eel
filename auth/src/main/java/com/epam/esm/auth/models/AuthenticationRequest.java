package com.epam.esm.auth.models;

public record AuthenticationRequest(
        String email,
        String password) {
}