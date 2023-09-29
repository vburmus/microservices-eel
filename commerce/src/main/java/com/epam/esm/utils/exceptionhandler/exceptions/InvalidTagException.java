package com.epam.esm.utils.exceptionhandler.exceptions;

public class InvalidTagException extends RuntimeException {
    public InvalidTagException(String message) {
        super(message);
    }
}