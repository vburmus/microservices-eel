package com.epam.esm.mail

import com.epam.esm.utils.amqp.dto.PurchaseDTO

interface EmailService {
    fun sendOrderConfirmationEmail(to:String, purchaseDTO: PurchaseDTO)
    fun sendEmailVerificationEmail(to: String, activationUrl: String)
}