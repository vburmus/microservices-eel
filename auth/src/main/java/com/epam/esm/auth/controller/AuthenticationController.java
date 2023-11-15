package com.epam.esm.auth.controller;

import com.epam.esm.auth.models.AuthenticationRequest;
import com.epam.esm.auth.models.RegisterRequest;
import com.epam.esm.auth.models.TokenDTO;
import com.epam.esm.auth.service.AuthenticationService;
import com.epam.esm.credentials.service.CredentialsService;
import com.epam.esm.jwt.service.JwtService;
import com.epam.esm.model.UserDTO;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.esm.utils.Constants.AUTHENTICATION_BEARER_TOKEN;
import static com.epam.esm.utils.Constants.AUTHORIZATION_HEADER;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final CredentialsService credentialsService;
    private final JwtService jwtService;

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> register(@Valid @RequestPart("request") RegisterRequest request,
                                           @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(authenticationService.register(request, image));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Void> refreshToken(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken,
                                             HttpServletResponse response) {
        jwtService.validateToken(bearerToken);
        String newAccessToken = authenticationService.refreshToken(bearerToken.substring(7));
        response.setHeader(AUTHORIZATION_HEADER, AUTHENTICATION_BEARER_TOKEN + newAccessToken);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/activate-account")
    public ResponseEntity<Void> activateAccount(@RequestParam String token) {
        credentialsService.activateAccount(token);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<Void> deleteAccount(@RequestParam("email") String email) {
        credentialsService.delete(email);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getUserFromJwt(@RequestHeader(AUTHORIZATION_HEADER) String bearerToken) {
        jwtService.validateToken(bearerToken);
        return ResponseEntity.ok(authenticationService.decodeUserFromJwt(bearerToken.substring(7)));
    }
}