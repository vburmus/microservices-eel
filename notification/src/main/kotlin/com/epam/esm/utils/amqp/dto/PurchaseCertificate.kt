package com.epam.esm.utils.amqp.dto

data class PurchaseCertificate(
    var certificate: CertificateDTO = CertificateDTO(),
    var quantity:Int = 0
)