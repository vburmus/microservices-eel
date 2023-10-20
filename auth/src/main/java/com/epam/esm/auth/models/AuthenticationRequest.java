package com.epam.esm.auth.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import static com.epam.esm.utils.Constants.EMAIL_BAD_FORMED;
import static com.epam.esm.utils.Constants.EMAIL_CAN_T_BE_BLANK;

public record AuthenticationRequest(
        @Email(message = EMAIL_BAD_FORMED)
        @NotBlank(message = EMAIL_CAN_T_BE_BLANK)
        String email,
        @NotBlank(message = EMAIL_CAN_T_BE_BLANK)
        String password) {
}