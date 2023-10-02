package com.epam.esm.tag.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import static com.epam.esm.utils.Constants.NAME_CANNOT_BE_EMPTY;
import static com.epam.esm.utils.Constants.NAME_SHOULD_BE_LESS_THAN_30_CHARS;

public record TagDTO(
        Long id,
        @NotBlank(message = NAME_CANNOT_BE_EMPTY)
        @Size(message = NAME_SHOULD_BE_LESS_THAN_30_CHARS)
        String name,
        String imageUrl) {
}