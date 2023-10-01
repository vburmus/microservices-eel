package com.epam.esm.utils.exceptionhandler.exceptions;

public class RestApiServerException extends RuntimeException {
    public RestApiServerException(String message) {
        super(message);
    }
}