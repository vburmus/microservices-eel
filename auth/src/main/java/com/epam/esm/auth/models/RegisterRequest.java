package com.epam.esm.auth.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.epam.esm.utils.Constants.*;

public record RegisterRequest(
        @Size(max = 30, message = NAME_SHOULD_BE_LESS_THAN_30_CHARS)
        @NotBlank(message = NAME_CAN_T_BE_BLANK)
        String name,
        @Size(max = 30, message = SURNAME_SHOULD_BE_LESS_THAN_30_CHARS)
        @NotBlank(message = SURNAME_CAN_T_BE_BLANK)
        String surname,
        @Size(max = 15, message = PHONE_SHOULD_BE_LESS_THAN_15_CHARS)
        @NotBlank(message = PHONE_CAN_T_BE_BLANK)
        String phone,
        @Size(max = 255, message = EMAIL_BE_LESS_THAN_255_CHARS)
        @NotBlank(message = EMAIL_CAN_T_BE_BLANK)
        @Email(message = EMAIL_BAD_FORMED)
        String email,
        @NotBlank(message = PASSWORD_CAN_T_BE_BLANK)
        String password) {
}