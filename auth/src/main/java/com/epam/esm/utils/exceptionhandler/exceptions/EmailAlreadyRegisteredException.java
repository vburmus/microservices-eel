package com.epam.esm.utils.exceptionhandler.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String e) {
        super(e);
    }
}