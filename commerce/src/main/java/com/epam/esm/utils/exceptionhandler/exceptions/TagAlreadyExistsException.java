package com.epam.esm.utils.exceptionhandler.exceptions;

public class TagAlreadyExistsException extends RuntimeException {
    public TagAlreadyExistsException(String message) {
        super(message);
    }
}