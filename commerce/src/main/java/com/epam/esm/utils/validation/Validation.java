package com.epam.esm.utils.validation;

import com.epam.esm.utils.exceptionhandler.exceptions.InvalidFileException;
import com.epam.esm.utils.exceptionhandler.exceptions.NullableFileException;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import static com.epam.esm.utils.Constants.*;

@UtilityClass
public class Validation {
    public static void validateImage(MultipartFile image) {
        if (image.getContentType() == null) throw new NullableFileException(FILE_CAN_T_BE_NULL);
        String fileName = image.getOriginalFilename();
        if (fileName == null) throw new InvalidFileException(INVALID_FILE_CHECK_NAME);
        int index = fileName.lastIndexOf('.');
        String extension = "";
        if (index > 0) {
            extension = fileName.substring(index);
        }
        if (extension.isEmpty() || !ALLOWED_IMG_EXTENSIONS.contains(extension)) {
            throw new InvalidFileException(INVALID_FILE_CHECK_EXTENSION);
        }
    }
}
