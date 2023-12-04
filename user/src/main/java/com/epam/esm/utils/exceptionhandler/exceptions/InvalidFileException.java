package com.epam.esm.utils.exceptionhandler.exceptions;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String e) {
        super(e);
    }
}