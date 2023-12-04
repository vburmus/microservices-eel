package com.epam.esm.utils.config.security;

import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.service.CustomUserCredentialsService;
import com.epam.esm.jwt.service.JwtService;
import com.google.gson.JsonObject;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static com.epam.esm.utils.AuthConstants.*;
import static com.epam.esm.utils.Constants.TOKEN_EXPIRED;

@RequiredArgsConstructor
@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private final CustomUserCredentialsService userDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(AUTHENTICATION_BEARER_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String email = jwtService.extractUsername(bearerToken.substring(7));
            Credentials credentials = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(credentials, credentials.getPassword(),
                            Collections.singleton(new SimpleGrantedAuthority(credentials.getRole().name())));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty(TITLE, AUTHENTICATION_EXCEPTION);
            errorJson.addProperty(STATUS, HttpServletResponse.SC_UNAUTHORIZED);
            errorJson.addProperty(DETAIL, TOKEN_EXPIRED);
            response.getWriter().write(errorJson.toString());
        }
    }
}