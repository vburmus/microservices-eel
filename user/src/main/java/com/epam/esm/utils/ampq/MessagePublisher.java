package com.epam.esm.utils.ampq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessagePublisher {
    private final RabbitTemplate template;
    @Value("${user.image.exchange}")
    private String userImageExchange;
    @Value("${user.image.key}")
    private String userImageRoutingKey;

    public void publishUserImage(ImageUploadRequest request) {
        template.convertAndSend(userImageExchange, userImageRoutingKey, request);
    }
}