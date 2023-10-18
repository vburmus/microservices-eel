package com.epam.esm.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String API_CALL_ERROR = "API Call Error";
    public static final String ALREADY_EXIST_ERROR = "Already Exist Error";
    public static final String INVALID_ITEM_ERROR = "Invalid Item Error";
    public static final String UPDATE_ERROR = "Update Error";
    public static final String PURCHASE_ERROR = "Purchase Error";
    public static final String NO_SUCH_ITEM_ERROR = "No Such Item Error";
    public static final String TAG_ALREADY_EXISTS = "Tag with name %s already exists";
    public static final String TAG_DOESNT_EXIST_ID = "Tag with id %d doesn't exist";
    public static final String TAGS = "tags";
    public static final String CERTIFICATE_EXISTS = "Certificate with name %s and duration %s exist";
    public static final String CERTIFICATE_DOES_NOT_EXISTS_ID = "Certificate with id %d " +
            "doesn't exist";
    public static final String CREATE_DATE = "create_date";
    public static final String LAST_UPDATE = "last_update_date";
    public static final String ID = "id";
    public static final String PRICE = "price";
    public static final String NAME = "name";
    public static final String SHORT_DESCRIPTION = "short_description";
    public static final String DURATION_DATE = "duration_date";
    public static final String SORT_PARAMETER_ERROR = "Sort Parameter Error";
    public static final String CERTIFICATES = "certificates";
    public static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String TAG_DOESNT_EXIST_NAME = "Tag with name %s doesn't exist";
    public static final String UPDATE_CERTIFICATE_IS_NULL = "Error updating certificate. Updated certificate is null.";
    public static final String GET_GC_BY_TAGS_AND_PART =
            "SELECT DISTINCT id,name,short_description,long_description,price,image_url, duration_date," +
                    "create_date,last_update_date FROM " +
                    "Certificate AS c JOIN certificate_tags AS t ON c.id=" +
                    "t.certificate_id WHERE tag_id IN :tags AND" +
                    "(short_description LIKE '%' || :partial || '%' OR name LIKE '%' || :partial || '%')";
    public static final String QUERY_FIND_BY_PART_NAME_OR_DESCRIPTION = "SELECT c FROM Certificate c WHERE c.name " +
            "LIKE %:partialNameOrShortDescription% OR c.shortDescription LIKE %:partialNameOrShortDescription%";
    public static final String PURCHASE_DOES_NOT_EXISTS_ID = "Purchase with id %d " +
            "doesn't exist";
    public static final String JSON_EXCEPTION = "Json Exception";
    public static final String SHOULD_HAVE_AT_LEAST_ONE_TAG = "Should have at least one tag";
    public static final String SHOULD_HAVE_TAGS = "Should have tags";
    public static final String DATE_SHOULD_BE_IN_FUTURE = "Date should be in future";
    public static final String LONG_DESCRIPTION_SHOULD_BE_LESS_THAN_600_CHARS = "Long description should be less than" +
            " 600 chars";
    public static final String SHORT_DESCRIPTION_SHOULD_BE_LESS_THAN_50_CHARS = "Short description should be less " +
            "than 50 chars";
    public static final String NAME_SHOULD_BE_LESS_THAN_15_CHARS = "Name should be less than 15 chars";
    public static final String SHORT_DESCRIPTION_CANNOT_BE_BLANK = "Short description cannot be blank";
    public static final String NAME_CANNOT_BE_BLANK = "Name cannot be blank";
    public static final String QUANTITY_CANNOT_BE_NULL = "Quantity cannot be null";
    public static final String QUANTITY_MUST_BE_GREATER_THAN_ZERO = "Quantity must be greater than zero";
    public static final String CERTIFICATE_ID_CANNOT_BE_NULL = "Certificate id cannot be null";
    public static final String USER_ID_CANNOT_BE_EMPTY = "User Id cannot be empty";
    public static final String PURCHASE_SHOULD_CONTAIN_CERTIFICATES = "Purchase should contain certificates";
    public static final String DESCRIPTION_SHOULD_BE_LESS_THAN_255_CHARS = "Description should be less than 255 chars";
    public static final String NAME_SHOULD_BE_LESS_THAN_30_CHARS = "Name should be less than 30 chars";
    public static final String NAME_CANNOT_BE_EMPTY = "Name cannot be empty";
}