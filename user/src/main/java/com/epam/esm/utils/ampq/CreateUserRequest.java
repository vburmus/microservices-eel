package com.epam.esm.utils.ampq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.epam.esm.utils.Constants.*;
import static com.epam.esm.utils.Constants.EMAIL_CAN_T_BE_BLANK;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private Long id;
    @Size(max = 30, message = NAME_SHOULD_BE_LESS_THAN_30_CHARS)
    @NotBlank(message = NAME_CAN_T_BE_BLANK)
    String name;
    @Size(max = 30, message = SURNAME_SHOULD_BE_LESS_THAN_30_CHARS)
    @NotBlank(message = SURNAME_CAN_T_BE_BLANK)
    String surname;
    @Size(max = 255, message = EMAIL_BE_LESS_THAN_255_CHARS)
    @NotBlank(message = EMAIL_CAN_T_BE_BLANK)
    String email;
    @Size(max = 15, message = PHONE_SHOULD_BE_LESS_THAN_15_CHARS)
    @NotBlank(message = PHONE_CAN_T_BE_BLANK)
    String phone;
}