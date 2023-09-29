package com.epam.esm.utils;


import com.epam.esm.tag.models.Tag;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Validation {
    public static boolean isTagValid(Tag tag) {
        return tag != null && tag.getName() != null && !tag.getName().isEmpty();
    }
}