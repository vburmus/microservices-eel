package com.epam.esm.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String ID = "id";
    public static final String ROLE = "role";
    public static final String EPAM = "epam";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String TYPE = "type";
    public static final String ACCESS_TOKENS = "accessTokens";
    public static final String REFRESH_TOKENS = "refreshTokens";
    public static final String BLACK_LIST = "blackList";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHENTICATION_BEARER_TOKEN = "Bearer ";
    public static final String CACHE_NOT_FOUND = "Cache not found";
    public static final String MISSING_USER_EMAIL = "Missing user email";
    public static final String TOKEN_NEEDED = "Request should contain token.";
    public static final String EMAIL_IS_ALREADY_REGISTERED = "Email is already registered";
    public static final String NAME_SHOULD_BE_LESS_THAN_30_CHARS = "Name should be less than 30 chars";
    public static final String NAME_CAN_T_BE_BLANK = "Name can't be blank";
    public static final String SURNAME_SHOULD_BE_LESS_THAN_30_CHARS = "Surname should be less than 30 chars";
    public static final String SURNAME_CAN_T_BE_BLANK = "Surname can't be blank";
    public static final String PHONE_SHOULD_BE_LESS_THAN_15_CHARS = "Phone should be less than 15 chars";
    public static final String PHONE_CAN_T_BE_BLANK = "Phone can't be blank";
    public static final String EMAIL_BE_LESS_THAN_255_CHARS = "Email be less than 255 chars";
    public static final String EMAIL_CAN_T_BE_BLANK = "Email can't be blank";
    public static final String PASSWORD_CAN_T_BE_BLANK = "Email can't be blank";
    public static final String EMAIL_BAD_FORMED = "Email address is not a well formed email";
    public static final String TOKEN_HAS_BEEN_BANNED = "Token has been banned";
    public static final String INVALID_USER_ERROR = "Invalid User Error";
    public static final String AUTHENTICATION_EXCEPTION = "Authentication Exception";
    public static final String WRONG_EMAIL_OR_PASSWORD = "Wrong email or password";
    public static final String CREDENTIALS_ERROR = "Credentials Error";
    public static final String TOKEN_ERROR = "Invalid Token Error";
    public static final String USER_NOT_EXIST_EMAIL = "User with email %s not found";
    public static final String INVALID_VALIDATION_TOKEN = "Invalid validation token";
    public static final String ACCESS_DENIED = "Access denied";
}