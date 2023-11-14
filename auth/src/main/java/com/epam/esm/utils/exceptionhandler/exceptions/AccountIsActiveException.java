package com.epam.esm.utils.exceptionhandler.exceptions;

public class AccountIsActiveException extends RuntimeException {

    public static final String ACCOUNT_HAS_BEEN_ALREADY_ACTIVATED = "Account has been already activated";

    public AccountIsActiveException() {
        super(ACCOUNT_HAS_BEEN_ALREADY_ACTIVATED);
    }
}