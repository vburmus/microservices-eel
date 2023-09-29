package com.epam.esm.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {
    public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
    public static final String API_CALL_ERROR = "API Call Error";
    public static final String ALREADY_EXIST_ERROR = "Already Exist Error";
    public static final String INVALID_ITEM_ERROR = "Invalid Item Error";
    public static final String UPDATE_ERROR = "Update Error";
    public static final String NO_SUCH_ITEM_ERROR = "No Such Item Error";
    public static final String ERROR_WHILE_MAKING_API_CALL_TO = "Error while making API call to: ";
    public static final String AN_INTERNAL_SERVER_ERROR_OCCURRED_WHILE_PROCESSING_THE_REQUEST = "An internal server " +
            "error occurred while processing the request.";
    public static final String GENERIC_EXCEPTION = "Generic exception";
    public static final String TAG_ALREADY_EXISTS = "Tag with name %s already exists";
    public static final String TAG_DOESNT_EXIST_ID = "Tag with id %d doesn't exist";
    public static final String TAG_IS_INVALID = "Tag with name %s is invalid";
    public static final String TAGS = "tags";
    public static final String CERTIFICATE_IS_INVALID = "Gift certificate with name %s and duration %s is invalid";
    public static final String CERTIFICATE_EXISTS = "Gift certificate with name %s and duration %s exist";
    public static final String CERTIFICATE_DOES_NOT_EXISTS_ID = "Gift certificate with id %d " +
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
    public static final String CERTIFICATE_SHOULD_HAVE_AT_LEAST_ONE_TAG = "Certificate should have at least one tag.";
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

}