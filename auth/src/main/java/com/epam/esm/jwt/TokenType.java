package com.epam.esm.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public enum TokenType {
    ACCESS(Date.from(Instant.now().plus(60, ChronoUnit.MINUTES))),
    REFRESH(Date.from(Instant.now().plus(2, ChronoUnit.DAYS))),
    EMAIL_VALIDATION(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)));
    private final Date expiryDate;

    TokenType(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }
}