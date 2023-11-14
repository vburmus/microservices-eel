package com.epam.esm.utils.amqp;

import com.epam.esm.purchase.models.PurchaseDTO;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PurchaseCreationMessage {
    private String email;
    private PurchaseDTO purchaseDTO;
}