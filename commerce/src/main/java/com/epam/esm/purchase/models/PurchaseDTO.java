package com.epam.esm.purchase.models;

import com.epam.esm.purchasecertificate.model.PurchaseCertificate;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static com.epam.esm.utils.Constants.DATE_PATTERN;


public record PurchaseDTO(
        Long id,
        String description,
        BigDecimal cost,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        LocalDateTime createDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
        LocalDateTime lastUpdateDate,
        Long userId,
        Set<PurchaseCertificate> purchaseCertificates) {

}