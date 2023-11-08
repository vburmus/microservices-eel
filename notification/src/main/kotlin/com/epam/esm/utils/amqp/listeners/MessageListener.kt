package com.epam.esm.utils.amqp.listeners

import com.epam.esm.mail.EmailService
import com.epam.esm.utils.amqp.EmailValidationMessage
import com.epam.esm.utils.amqp.PurchaseCreationMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MessageListener(val emailService: EmailService) {
    @RabbitListener(queues = ["\${purchase.queue}"])
    fun onOrderCreation(message: PurchaseCreationMessage) {
        emailService.sendOrderConfirmationEmail(message.email, message.purchaseDTO)
    }

    @RabbitListener(queues = ["\${user.queue}"])
    fun onUserRegister(message: EmailValidationMessage) {
        emailService.sendEmailVerificationEmail(message.email, message.activationUrl)
    }
}