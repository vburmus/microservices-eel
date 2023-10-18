package com.epam.esm.auth.service;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.model.Role;
import com.epam.esm.credentials.service.CredentialsService;
import com.epam.esm.credentials.service.CustomUserCredentialsService;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.jwt.service.TokenGenerator;
import com.epam.esm.utils.EntityToDtoMapper;
import com.epam.esm.utils.exceptionhandler.exceptions.EmailNotFoundException;
import com.epam.esm.utils.openfeign.User;
import com.epam.esm.utils.openfeign.UserFeignClient;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public TokenDTO register(RegisterRequest request, MultipartFile image) {
        credentialsService.createCredentials(request);
        User user = userClient.create(entityToDtoMapper.toUserCreationRequest(request), image);
        user.setRole(Role.USER);
        return tokenGenerator.createToken(user);
    }

    public TokenDTO authenticate(AuthenticationRequest request) {
        User user = userClient.getByEmail(request.email());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(),
                request.password()));
        Credentials credentials = userDetailsService.loadUserByUsername(request.email());
        user.setRole(credentials.getRole());
        return tokenGenerator.createToken(user);
    }

    public String refreshToken(String jwt) {
        User user = getUserFromJwt(jwt);
        return tokenGenerator.createAccessToken(user);
    }

    public String getRole(String jwt) {
        return jwtService.extractRole(jwt);
    }

    private User getUserFromJwt(String jwt) {
        String email = jwtService.extractUsername(jwt);
        if (email == null) throw new EmailNotFoundException(MISSING_USER_EMAIL);
        return userClient.getByEmail(email);
    }
}