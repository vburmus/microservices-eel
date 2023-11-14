package com.epam.esm.mail

import com.epam.esm.utils.Constants.Companion.VOUCHER_VERSE
import com.epam.esm.utils.Constants.Companion.VOUCHER_VERSE_CONFIRMATION
import com.epam.esm.utils.Constants.Companion.VOUCHER_VERSE_VERIFICATION
import com.epam.esm.utils.amqp.dto.PurchaseDTO
import jakarta.mail.internet.InternetAddress
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailServiceImpl(
    val emailSender: JavaMailSender,
    val templateEngine: TemplateEngine,
    @Value("\${mail.address}")
    val email: String) : EmailService {
    override fun sendOrderConfirmationEmail(to: String, purchaseDTO: PurchaseDTO) {
        val context = Context()
        context.setVariable("purchase", purchaseDTO)
        val htmlContent = templateEngine.process("purchase-message", context)
        val message = MimeMessageHelper(emailSender.createMimeMessage(), true)
        message.setFrom(InternetAddress(email, VOUCHER_VERSE))
        message.setTo(to)
        message.setSubject(VOUCHER_VERSE_CONFIRMATION)
        message.setText(htmlContent, true)
        emailSender.send(message.mimeMessage)
    }

    override fun sendEmailVerificationEmail(to: String, activationUrl: String) {
        val context = Context()
        context.setVariable("activationUrl", activationUrl)
        val htmlContent = templateEngine.process("email-verification", context)
        val message = MimeMessageHelper(emailSender.createMimeMessage(), true)
        message.setTo(to)
        message.setFrom(email)
        message.setText(activationUrl)
        message.setFrom(InternetAddress(email, VOUCHER_VERSE))
        message.setSubject(VOUCHER_VERSE_VERIFICATION)
        message.setText(htmlContent, true)
        emailSender.send(message.mimeMessage)
    }
}