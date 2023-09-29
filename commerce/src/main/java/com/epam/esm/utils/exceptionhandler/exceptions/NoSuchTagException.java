package com.epam.esm.utils.exceptionhandler.exceptions;

public class NoSuchTagException extends RuntimeException {
    public NoSuchTagException(String exception) {
        super(exception);
    }
}