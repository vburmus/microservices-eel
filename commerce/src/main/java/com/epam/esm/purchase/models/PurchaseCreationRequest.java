package com.epam.esm.purchase.models;

import java.util.Map;

public record PurchaseCreationRequest(String description, Long userId, Map<Long, Integer> certificateQuantity) {
}