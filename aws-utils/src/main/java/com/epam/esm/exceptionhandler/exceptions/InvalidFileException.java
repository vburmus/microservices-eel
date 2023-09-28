package com.epam.esm.exceptionhandler.exceptions;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String e) {
        super(e);
    }
}