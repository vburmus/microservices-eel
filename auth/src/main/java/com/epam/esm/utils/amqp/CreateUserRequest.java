package com.epam.esm.utils.amqp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phone;
}