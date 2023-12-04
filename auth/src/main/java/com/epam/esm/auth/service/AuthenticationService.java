package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.model.AuthenticatedUser;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    String register(RegisterRequest request, MultipartFile image);

    TokenDTO authenticate(AuthenticationRequest request);

    String refreshToken(String jwt);

    AuthenticatedUser decodeUserFromJwt(String jwt);
}