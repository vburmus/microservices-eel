package com.epam.esm.config;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {
    public static final String AUTHORIZATION = "Authorization";
    public static final List<String> ALLOWED_HEADERS = List.of(AUTHORIZATION, "Content-Type");
    public static final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PATCH", "DELETE");
}