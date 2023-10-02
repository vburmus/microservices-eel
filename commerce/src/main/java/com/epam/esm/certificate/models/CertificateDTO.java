package com.epam.esm.certificate.models;

import com.epam.esm.tag.models.TagDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.utils.Constants.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
    private Long id;
    @Size(max = 15, message = NAME_SHOULD_BE_LESS_THAN_15_CHARS)
    @NotBlank(message = NAME_CANNOT_BE_BLANK)
    private String name;
    @Size(max = 50, message = SHORT_DESCRIPTION_SHOULD_BE_LESS_THAN_50_CHARS)
    @NotBlank(message = SHORT_DESCRIPTION_CANNOT_BE_BLANK)
    private String shortDescription;
    @Size(max = 600, message = LONG_DESCRIPTION_SHOULD_BE_LESS_THAN_600_CHARS)
    private String longDescription;
    private BigDecimal price;
    @NotNull(message = SHOULD_HAVE_TAGS)
    @NotEmpty(message = SHOULD_HAVE_AT_LEAST_ONE_TAG)
    private List<TagDTO> tags;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    @Future(message = DATE_SHOULD_BE_IN_FUTURE)
    private LocalDateTime durationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime lastUpdateDate;
}