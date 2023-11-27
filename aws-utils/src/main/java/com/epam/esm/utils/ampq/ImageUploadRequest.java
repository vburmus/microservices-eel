package com.epam.esm.utils.ampq;

public record ImageUploadRequest(byte[] imageBytes, Long userId) {
}