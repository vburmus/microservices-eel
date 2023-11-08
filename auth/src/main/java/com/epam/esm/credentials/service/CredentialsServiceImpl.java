package com.epam.esm.credentials.service;

import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.repository.CredentialsRepository;
import com.epam.esm.utils.exceptionhandler.exceptions.AccountIsActiveException;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailAlreadyRegisteredException;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.epam.esm.utils.Constants.EMAIL_IS_ALREADY_REGISTERED;
import static com.epam.esm.utils.Constants.USER_NOT_EXIST_EMAIL;

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
                .build();
        credentialsRepository.save(credentials);
    }

    @Override
    public void activateAccount(String email) {
        Credentials credentials =
                credentialsRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(USER_NOT_EXIST_EMAIL));
        if (credentials.isEnabled()) throw new AccountIsActiveException();
        credentials.enableAccount();
    }
}