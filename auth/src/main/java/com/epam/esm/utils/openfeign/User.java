package com.epam.esm.utils.openfeign;

import com.epam.esm.credentials.model.Provider;
import com.epam.esm.credentials.model.Role;
import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String email;
    private Provider provider;
    private Role role;
}