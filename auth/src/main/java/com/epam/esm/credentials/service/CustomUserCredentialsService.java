package com.epam.esm.credentials.service;

import com.epam.esm.credentials.model.Credentials;
import com.epam.esm.credentials.repository.CredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserCredentialsService implements UserDetailsService {
    private final CredentialsRepository credentialsRepository;

    @Override
    public Credentials loadUserByUsername(String email) {
        return credentialsRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not " +
                "found"));
    }
}
