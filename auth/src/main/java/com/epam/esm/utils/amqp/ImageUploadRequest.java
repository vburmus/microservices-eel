package com.epam.esm.utils.amqp;

public record ImageUploadRequest(byte[] imageBytes, Long userId) {
}