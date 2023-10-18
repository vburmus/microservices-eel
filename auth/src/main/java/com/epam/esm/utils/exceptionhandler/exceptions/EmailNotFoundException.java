package com.epam.esm.utils.exceptionhandler.exceptions;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String e) {
        super(e);
    }
}