package com.epam.esm.utils.exceptionhandler.exceptions;

public class TokenRequiredException extends RuntimeException {
    public TokenRequiredException(String e) {
        super(e);
    }
}