package com.epam.esm.credentials.service;

import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.credentials.model.Credentials;
import org.springframework.stereotype.Service;

@Service
public interface CredentialsService {
    Credentials create(RegisterRequest request);

    Credentials getByEmail(String email);

    void activateAccount(String email);

    void delete(String id);
}