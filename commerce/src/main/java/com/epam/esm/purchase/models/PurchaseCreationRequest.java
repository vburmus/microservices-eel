package com.epam.esm.purchase.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

import static com.epam.esm.utils.Constants.DESCRIPTION_SHOULD_BE_LESS_THAN_255_CHARS;
import static com.epam.esm.utils.Constants.PURCHASE_SHOULD_CONTAIN_CERTIFICATES;

public record PurchaseCreationRequest(
        @Size(max = 255, message = DESCRIPTION_SHOULD_BE_LESS_THAN_255_CHARS)
        String description,
        @NotEmpty(message = PURCHASE_SHOULD_CONTAIN_CERTIFICATES)
        Set<PurchasePosition> positions) {
}