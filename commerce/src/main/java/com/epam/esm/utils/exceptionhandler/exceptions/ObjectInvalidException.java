package com.epam.esm.utils.exceptionhandler.exceptions;

public class ObjectInvalidException extends RuntimeException {
    public ObjectInvalidException(String message) {
        super(message);
    }
}