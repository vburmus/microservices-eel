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
import com.epam.esm.utils.amqp.EmailValidationMessage;
import com.epam.esm.utils.amqp.MessagePublisher;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailNotFoundException;
import com.epam.esm.utils.openfeign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.esm.utils.Constants.MISSING_USER_EMAIL;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final JwtService jwtService;
    private final UserFeignClient userClient;
    private final CredentialsService credentialsService;
    private final EntityToDtoMapper entityToDtoMapper;
    private final CustomUserCredentialsService userDetailsService;
    private final MessagePublisher messagePublisher;
    @Value("${verification.address}")
    private String verificationUrl;

    @Transactional
    public String register(RegisterRequest request, MultipartFile image) {
        Credentials credentials;
        UserDTO user;
        try {
            credentials = credentialsService.getByEmail(request.email());
            user = userClient.getByEmail(credentials.getUsername());
        } catch (EmailNotFoundException e) {
            credentials = credentialsService.create(request);
            user = userClient.create(entityToDtoMapper.toUserCreationRequest(request), image);
            user.setRole(Role.USER);
        }
        String token = tokenGenerator.createValidationToken(user);
        EmailValidationMessage evm = EmailValidationMessage.builder()
                .email(credentials.getUsername())
                .activationUrl(verificationUrl + "?token=" + token)
                .build();
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
}