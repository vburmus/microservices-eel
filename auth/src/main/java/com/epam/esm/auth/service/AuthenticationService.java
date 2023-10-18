package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    TokenDTO register(RegisterRequest request, MultipartFile image);

    TokenDTO authenticate(AuthenticationRequest request);

    String refreshToken(String jwt);

    String getRole(String jwt);
}