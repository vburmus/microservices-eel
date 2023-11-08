package com.epam.esm.utils.amqp;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmailValidationMessage {
    private String email;
    private String activationUrl;
}