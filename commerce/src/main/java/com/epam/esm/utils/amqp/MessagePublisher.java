package com.epam.esm.utils.amqp;

import com.epam.esm.utils.exceptionhandler.exceptions.ImageUploadException;
import com.epam.esm.utils.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.epam.esm.utils.Constants.INVALID_FILE_CHECK_BYTES;

@RequiredArgsConstructor
@Service
public class MessagePublisher {
    private final RabbitTemplate template;
    @Value("${purchase.exchange}")
    private String purchaseExchange;
    @Value("${purchase.key}")
    private String purchaseRoutingKey;

    public void publishPurchaseCreationMessage(PurchaseCreationMessage message) {
        template.convertAndSend(purchaseExchange, purchaseRoutingKey, message);
    }

    public void publishImageUploadMessage(MultipartFile image, Long objectId, String exchange, String routingKey) {
        Validation.validateImage(image);
        try {
            ImageUploadRequest icr = new ImageUploadRequest(image.getBytes(), objectId);
            template.convertAndSend(exchange, routingKey, icr);
        } catch (IOException e) {
            throw new ImageUploadException(INVALID_FILE_CHECK_BYTES);
        }
    }
}