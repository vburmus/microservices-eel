package com.epam.esm.utils.amqp;

public record EmailValidationMessage(String email, String activationUrl) {
}