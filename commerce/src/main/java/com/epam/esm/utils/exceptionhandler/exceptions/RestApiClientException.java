package com.epam.esm.utils.exceptionhandler.exceptions;

public class RestApiClientException extends RuntimeException {
    public RestApiClientException(String message) {
        super(message);
    }

}