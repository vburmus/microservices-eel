package com.epam.esm.purchase.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import static com.epam.esm.utils.Constants.*;

public record PurchasePosition(
        @NotNull(message = CERTIFICATE_ID_CANNOT_BE_NULL)
        Long certificateId,
        @NotNull(message = QUANTITY_CANNOT_BE_NULL)
        @Positive(message = QUANTITY_MUST_BE_GREATER_THAN_ZERO)
        Integer quantity) {
}
