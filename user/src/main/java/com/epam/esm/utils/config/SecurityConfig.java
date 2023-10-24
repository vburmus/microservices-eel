package com.epam.esm.utils.config;

import com.epam.esm.filter.ServiceAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.epam.esm.utils.AuthConstants.ADMIN;
import static com.epam.esm.utils.AuthConstants.USER;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ServiceAuthenticationFilter serviceAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(serviceAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request.
                                requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/users").hasAuthority(ADMIN)
                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/v1/users/**").anonymous()
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/v1/users").anonymous()
                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/v1/users/**").hasAnyAuthority(USER, ADMIN)
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/v1/users/**").hasAnyAuthority(USER, ADMIN));
        return http.build();
    }
}
