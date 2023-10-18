package com.epam.esm.credentials.service;

import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.model.Provider;
import com.epam.esm.credentials.model.Role;
import com.epam.esm.credentials.repository.CredentialsRepository;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailAlreadyRegisteredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.epam.esm.utils.Constants.EMAIL_IS_ALREADY_REGISTERED;

@Service
@RequiredArgsConstructor
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createCredentials(RegisterRequest request) {
        credentialsRepository.findByEmail(request.email()).ifPresent(req -> {
            throw new EmailAlreadyRegisteredException(EMAIL_IS_ALREADY_REGISTERED);
        });

        Credentials credentials = Credentials.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .provider(Provider.LOCAL)
                .role(Role.USER)
                .build();
        credentialsRepository.save(credentials);
    }
}