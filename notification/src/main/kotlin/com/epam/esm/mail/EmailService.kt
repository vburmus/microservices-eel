package com.epam.esm.mail

import com.epam.esm.utils.amqp.dto.PurchaseDTO

interface EmailService {
    fun sendHtmlEmail(to:String, purchaseDTO: PurchaseDTO)
}