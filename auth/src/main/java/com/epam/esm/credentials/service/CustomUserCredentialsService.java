package com.epam.esm.credentials.service;

import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.epam.esm.utils.Constants.USER_NOT_EXIST_EMAIL;


@Service
@RequiredArgsConstructor
public class CustomUserCredentialsService implements UserDetailsService {
    private final CredentialsRepository credentialsRepository;

    @Override
    public Credentials loadUserByUsername(String email) {
        return credentialsRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format(USER_NOT_EXIST_EMAIL, email)));
    }
}