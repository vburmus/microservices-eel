package com.epam.esm.credentials.repository;

import com.epam.esm.credentials.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialsRepository extends JpaRepository<Credentials, Long> {
    Optional<Credentials> findByEmail(String email);

    boolean existsByEmail(String email);
}