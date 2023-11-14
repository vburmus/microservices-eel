package com.epam.esm.utils.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessagePublisher {
    private final RabbitTemplate template;
    @Value("${purchase.exchange}")
    private String purchaseExchange;
    @Value("${purchase.key}")
    private String routingKey;

    public void publishMessage(PurchaseCreationMessage message) {
        template.convertAndSend(purchaseExchange,
                routingKey, message);
    }
}