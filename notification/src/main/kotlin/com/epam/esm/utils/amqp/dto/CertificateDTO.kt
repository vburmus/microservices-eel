package com.epam.esm.utils.amqp.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class CertificateDTO(
    var id: Long = -1,
    var name: String = "",
    var shortDescription: String? = "",
    var longDescription: String? = "",
    var price: BigDecimal = BigDecimal(-1),
    var imageUrl: String = "",
    var tags: List<TagDTO> = emptyList(),
    var durationDate: LocalDateTime = LocalDateTime.MIN,
    var createDate: LocalDateTime? = LocalDateTime.MIN,
    var lastUpdateDate: LocalDateTime? = LocalDateTime.MIN,
)