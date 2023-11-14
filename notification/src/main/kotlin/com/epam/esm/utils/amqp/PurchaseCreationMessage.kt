package com.epam.esm.utils.amqp

import com.epam.esm.utils.amqp.dto.PurchaseDTO

data class PurchaseCreationMessage(
    val email:String = "",
    val purchaseDTO: PurchaseDTO = PurchaseDTO()
)