package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.model.UserDTO;
import org.springframework.web.multipart.MultipartFile;

public interface AuthenticationService {
    String register(RegisterRequest request, MultipartFile image);

    TokenDTO authenticate(AuthenticationRequest request);

    String refreshToken(String jwt);

    UserDTO decodeUserFromJwt(String jwt);

    void activateAccount(String token);
}