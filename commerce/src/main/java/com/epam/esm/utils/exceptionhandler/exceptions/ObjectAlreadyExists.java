package com.epam.esm.utils.exceptionhandler.exceptions;

public class ObjectAlreadyExists extends RuntimeException {
    public ObjectAlreadyExists(String message) {
        super(message);
    }
}