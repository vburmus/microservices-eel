package com.epam.esm.utils.amqp


data class EmailValidationMessage(
    val email: String = "",
    val activationUrl: String = ""
)