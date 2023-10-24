package com.epam.esm.utils.exceptionhandler.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String e) {
        super(e);
    }
}