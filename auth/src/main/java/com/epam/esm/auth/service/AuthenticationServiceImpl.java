package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.service.CredentialsService;
import com.epam.esm.credentials.service.CustomUserCredentialsService;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.jwt.service.TokenGenerator;
import com.epam.esm.model.AuthenticatedUser;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.amqp.CreateUserRequest;
import com.epam.esm.utils.amqp.EmailValidationMessage;
import com.epam.esm.utils.amqp.ImageUploadRequest;
import com.epam.esm.utils.amqp.MessagePublisher;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailNotFoundException;
import com.epam.esm.utils.exceptionhandler.exceptions.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.epam.esm.utils.Constants.MISSING_USER_EMAIL;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final JwtService jwtService;
    private final CredentialsService credentialsService;
    private final EntityToDtoMapper entityToDtoMapper;
    private final CustomUserCredentialsService userDetailsService;
    private final MessagePublisher messagePublisher;
    @Value("${verification.address}")
    private String verificationUrl;

    @Transactional
    public String register(RegisterRequest request, MultipartFile image) {
        Credentials credentials;
        if (credentialsService.existsByEmail(request.email())) {
            credentials = credentialsService.getByEmail(request.email());
        } else {
            credentials = credentialsService.create(request);
            createUser(request, credentials);
            uploadUserImage(image, credentials.getId());
        }
        String token = tokenGenerator.createValidationToken(credentials);
        String verificationLink = verificationUrl + token;
        EmailValidationMessage evm = new EmailValidationMessage(credentials.getUsername(), verificationLink);
        messagePublisher.publishValidateEmailMessage(evm);
        return token;
    }

    public TokenDTO authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                request.password()));
        Credentials credentials = userDetailsService.loadUserByUsername(request.email());
        return tokenGenerator.createToken(credentials);
    }

    public String refreshToken(String jwt) {
        String email = jwtService.extractUsername(jwt);
        Credentials credentials = userDetailsService.loadUserByUsername(email);
        return tokenGenerator.createAccessToken(credentials);
    }

    public AuthenticatedUser decodeUserFromJwt(String jwt) {
        String email = jwtService.extractUsername(jwt);
        if (email == null) throw new EmailNotFoundException(MISSING_USER_EMAIL);
        return entityToDtoMapper.toAuthenticatedUser(credentialsService.getByEmail(email));
    }

    private void uploadUserImage(MultipartFile image, Long id) {
        if (!image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                ImageUploadRequest icr = new ImageUploadRequest(imageBytes, id);
                messagePublisher.publishImage(icr);
            } catch (IOException e) {
                throw new ImageUploadException(e.getMessage());
            }
        }
    }

    private void createUser(RegisterRequest request, Credentials credentials) {
        CreateUserRequest cur = entityToDtoMapper.toUserCreationRequest(request);
        cur.setId(credentials.getId());
        messagePublisher.publishUserCreationMessage(cur);
    }
}