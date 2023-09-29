package com.epam.esm.utils.exceptionhandler.exceptions;

public class NoSuchObjectException extends RuntimeException {
    public NoSuchObjectException(String exception) {
        super(exception);
    }
}