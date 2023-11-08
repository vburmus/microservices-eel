package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.service.CredentialsService;
import com.epam.esm.credentials.service.CustomUserCredentialsService;
import com.epam.esm.jwt.TokenType;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.jwt.service.TokenGenerator;
import com.epam.esm.model.Role;
import com.epam.esm.model.UserDTO;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.amqp.EmailValidationMessage;
import com.epam.esm.utils.amqp.MessagePublisher;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailNotFoundException;
import com.epam.esm.utils.exceptionhandler.exceptions.InvalidTokenException;
import com.epam.esm.utils.openfeign.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.esm.utils.Constants.INVALID_VALIDATION_TOKEN;
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
        credentialsService.createCredentials(request);
        UserDTO user = userClient.create(entityToDtoMapper.toUserCreationRequest(request), image);
        user.setRole(Role.USER);
        String token = tokenGenerator.createValidationToken(user);
        EmailValidationMessage evm = EmailValidationMessage.builder()
                .email(user.getEmail())
                .activationUrl(verificationUrl + "?token=" + token)
                .build();
        messagePublisher.publishMessage(evm);
        return token;
    }

    public TokenDTO authenticate(AuthenticationRequest request) {
        UserDTO user = userClient.getByEmail(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                request.password()));
        Credentials credentials = userDetailsService.loadUserByUsername(request.email());
        user.setRole(credentials.getRole());
        return tokenGenerator.createToken(user);
    }

    public String refreshToken(String jwt) {
        UserDTO user = getUserFromJwt(jwt);
        return tokenGenerator.createAccessToken(user);
    }

    public UserDTO decodeUserFromJwt(String jwt) {
        UserDTO user = getUserFromJwt(jwt);
        Credentials credentials = userDetailsService.loadUserByUsername(user.getEmail());
        user.setRole(credentials.getRole());
        user.setProvider(credentials.getProvider());
        return user;
    }

    private UserDTO getUserFromJwt(String jwt) {
        String email = jwtService.extractUsername(jwt);
        if (email == null) throw new EmailNotFoundException(MISSING_USER_EMAIL);
        return userClient.getByEmail(email);
    }
}