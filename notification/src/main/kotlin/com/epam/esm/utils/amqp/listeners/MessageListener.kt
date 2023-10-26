package com.epam.esm.utils.amqp.listeners

import com.epam.esm.mail.EmailService
import com.epam.esm.utils.amqp.PurchaseCreationMessage
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MessageListener(val emailService: EmailService) {
    @RabbitListener(queues = ["\${purchase.queue}"])
    fun onOrderCreation(message: PurchaseCreationMessage) {
        emailService.sendHtmlEmail(message.email, message.purchaseDTO)
    }
}