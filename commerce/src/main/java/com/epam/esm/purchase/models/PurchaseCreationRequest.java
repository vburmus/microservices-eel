package com.epam.esm.purchase.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.epam.esm.utils.Constants.*;

public record PurchaseCreationRequest(
        @Size(max = 255, message = DESCRIPTION_SHOULD_BE_LESS_THAN_255_CHARS)
        String description,
        @NotNull(message = USER_ID_CANNOT_BE_EMPTY)
        Long userId,
        @NotEmpty(message = PURCHASE_SHOULD_CONTAIN_CERTIFICATES)
        Set<PurchasePosition> positions) {
}