package com.epam.esm.credentials.service;

import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.repository.CredentialsRepository;
import com.epam.esm.jwt.TokenType;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.utils.amqp.MessagePublisher;
import com.epam.esm.utils.exceptionhandler.exceptions.AccountIsActiveException;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailAlreadyRegisteredException;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailNotFoundException;
import com.epam.esm.utils.exceptionhandler.exceptions.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.esm.model.Role.ADMIN;
import static com.epam.esm.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class CredentialsServiceImpl implements CredentialsService {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MessagePublisher messagePublisher;


    @Override
    public Credentials create(RegisterRequest request) {
        credentialsRepository.findByEmail(request.email()).ifPresent(req -> {
            throw new EmailAlreadyRegisteredException(EMAIL_IS_ALREADY_REGISTERED);
        });
        Credentials credentials = Credentials.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .build();
        return credentialsRepository.save(credentials);
    }

    @Override
    public Credentials getByEmail(String email) {
        return credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotFoundException(USER_NOT_EXIST_EMAIL));
    }

    @Override
    @Transactional
    public void activateAccount(String token) {
        String email = jwtService.extractUsername(token);
        if (jwtService.extractType(token) != TokenType.EMAIL_VALIDATION || email.isEmpty()) {
            throw new InvalidTokenException(INVALID_VALIDATION_TOKEN);
        }
        Credentials credentials =
                credentialsRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(USER_NOT_EXIST_EMAIL));
        if (credentials.isEnabled()) throw new AccountIsActiveException();
        credentials.enableAccount();
    }

    @Override
    public void delete(String email) {
        Credentials authenticationCredentials =
                (Credentials) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(authenticationCredentials.getUsername().equals(email) || authenticationCredentials.getRole().equals(ADMIN))) {
            throw new AccessDeniedException(ACCESS_DENIED);
        }
        Credentials credentials =
                credentialsRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException(USER_NOT_EXIST_EMAIL));
        credentialsRepository.delete(credentials);
        messagePublisher.publishUserDeletionMessage(credentials.getUsername());
    }
}