package com.epam.esm.certificate.models;

import com.epam.esm.tag.models.TagDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.epam.esm.utils.Constants.DATE_PATTERN;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private String longDescription;
    private BigDecimal price;
    private List<TagDTO> tags;
    private String imageUrl;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime durationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    private LocalDateTime lastUpdateDate;
}