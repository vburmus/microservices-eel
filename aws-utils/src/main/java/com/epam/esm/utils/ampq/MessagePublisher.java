package com.epam.esm.utils.ampq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessagePublisher {
    private final RabbitTemplate template;
    @Value("${user.image.response.exchange}")
    private String userImageExchange;
    @Value("${user.image.response.key}")
    private String userImageRoutingKey;

    public void publishLoadedImageResponse(ImageUploadResponse imageUploadResponse) {
        template.convertAndSend(userImageExchange, userImageRoutingKey, imageUploadResponse);
    }
}