package com.epam.esm.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {
    public static final String USER_DOESNT_EXIST_ID = "User with id %d doesn't exist";
    public static final String USER_DOESNT_EXIST_EMAIL = "User with email %s doesn't exist";
    public static final String ALREADY_REGISTERED = "User with email %s already registered";
    public static final String UPDATE_USER_IS_NULL = "Error updating user. Updated user is null.";
    public static final String JSON_EXCEPTION = "Json Exception";
    public static final String ALREADY_EXIST_ERROR = "Already Exist Error";
    public static final String INVALID_USER_ERROR = "Invalid User Error";
    public static final String UPDATE_ERROR = "Update Error";
    public static final String NO_SUCH_USER_ERROR = "No Such User Error";
    public static final String SORT_PARAMETER_ERROR = "Sort Parameter Error";
    public static final String NAME_SHOULD_BE_LESS_THAN_30_CHARS = "Name should be less than 30 chars";
    public static final String NAME_CAN_T_BE_BLANK = "Name can't be blank";
    public static final String SURNAME_SHOULD_BE_LESS_THAN_30_CHARS = "Surname should be less than 30 chars";
    public static final String SURNAME_CAN_T_BE_BLANK = "Surname can't be blank";
    public static final String PHONE_SHOULD_BE_LESS_THAN_15_CHARS = "Phone should be less than 15 chars";
    public static final String PHONE_CAN_T_BE_BLANK = "Phone can't be blank";
    public static final String EMAIL_BE_LESS_THAN_255_CHARS = "Email be less than 255 chars";
    public static final String EMAIL_CAN_T_BE_BLANK = "Email can't be blank";
    public static final String OPERATION_NOT_ALLOWED = "Operation not allowed";
    public static final List<String> ALLOWED_IMG_EXTENSIONS = List.of(".jpg", ".jpeg", ".png", ".gif");
    public static final String INVALID_FILE_CHECK_NAME = "Invalid file, check name";
    public static final String FILE_UPLOAD_ERROR = "File Upload Error";
    public static final String INVALID_FILE_CHECK_EXTENSION = "Invalid file, check extension";
    public static final String FILE_CAN_T_BE_NULL = "File can't be null";
    public static final String INVALID_FILE_CHECK_BYTES = "Invalid file, check bytes";
}