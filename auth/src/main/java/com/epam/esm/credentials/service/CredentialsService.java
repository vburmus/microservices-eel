package com.epam.esm.credentials.service;

import com.epam.esm.auth.models.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface CredentialsService {
    void createCredentials(RegisterRequest request);

    void activateAccount(String email);

    void delete(String id);
}