package com.epam.esm.utils.exceptionhandler.exceptions;

public class NoSuchUserException extends RuntimeException{

    public NoSuchUserException(String message) {
        super(message);
    }
}