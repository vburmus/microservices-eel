package com.epam.esm.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Constants {
    public static final String FILE_UPLOAD_ERROR = "File Upload Error";
    public static final List<String> ALLOWED_IMG_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif");
    public static final String INVALID_FILE_CHECK_NAME = "Invalid file, check name";
    public static final String NULLABLE_FILE = "Nullable File Error";
    public static final String INVALID_FILE_CHECK_EXTENSION = "Invalid file, check extension";
    public static final String FILE_CAN_T_BE_NULL = "File can't be null";
    public static final String DIRECTORY_SWAGGER_DESCRIPTION = "Name of directory in AWS S3 bucket, without / ";
}