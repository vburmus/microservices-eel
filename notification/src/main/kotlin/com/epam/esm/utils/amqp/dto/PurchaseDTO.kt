package com.epam.esm.utils.amqp.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class PurchaseDTO(
    var id: Long = -1,
    var description: String? = "",
    var cost: BigDecimal = BigDecimal(-1),
    var createDate: LocalDateTime = LocalDateTime.MIN,
    var lastUpdateDate: LocalDateTime = LocalDateTime.MIN,
    var userId: Long = -1,
    var purchaseCertificates: Set<PurchaseCertificate> = emptySet()
)