package com.epam.esm.certificate.models;

import com.epam.esm.tag.models.TagDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static com.epam.esm.utils.Constants.*;

public record CertificateDTO(
        Long id,
        @Size(max = 15, message = NAME_SHOULD_BE_LESS_THAN_15_CHARS)
        @NotBlank(message = NAME_CANNOT_BE_BLANK)
        String name,
        @Size(max = 50, message = SHORT_DESCRIPTION_SHOULD_BE_LESS_THAN_50_CHARS)
        @NotBlank(message = SHORT_DESCRIPTION_CANNOT_BE_BLANK)
        String shortDescription,
        @Size(max = 600, message = LONG_DESCRIPTION_SHOULD_BE_LESS_THAN_600_CHARS)
        String longDescription,
        BigDecimal price,
        @NotNull(message = SHOULD_HAVE_TAGS)
        @NotEmpty(message = SHOULD_HAVE_AT_LEAST_ONE_TAG)
        @Valid
        Set<TagDTO> tags,
        String imageUrl,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        @Future(message = DATE_SHOULD_BE_IN_FUTURE)
        LocalDateTime durationDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        LocalDateTime createDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        LocalDateTime lastUpdateDate) {
}