package com.epam.esm.utils.exceptionhandler.exceptions;

import lombok.Getter;

@Getter
public class FileUploadException extends RuntimeException {
    private final int httpStatus;

    public FileUploadException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}