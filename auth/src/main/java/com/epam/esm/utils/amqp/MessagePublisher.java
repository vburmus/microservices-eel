package com.epam.esm.utils.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessagePublisher {
    private final RabbitTemplate template;

    @Value("${user.validation.exchange}")
    private String emailValidationExchange;
    @Value("${user.validation.key}")
    private String emailValidationRoutingKey;
    @Value("${user.deletion.exchange}")
    private String userDeletionExchange;
    @Value("${user.deletion.key}")
    private String userDeletionRoutingKey;
    @Value("${user.creation.exchange}")
    private String userCreationExchange;
    @Value("${user.creation.key}")
    private String userCreationRoutingKey;
    @Value("${user.image.exchange}")
    private String userImageExchange;
    @Value("${user.image.key}")
    private String userImageRoutingKey;

    public void publishValidateEmailMessage(EmailValidationMessage message) {
        template.convertAndSend(emailValidationExchange, emailValidationRoutingKey, message);
    }

    public void publishUserDeletionMessage(String email) {
        template.convertAndSend(userDeletionExchange, userDeletionRoutingKey, email);
    }

    public void publishUserCreationMessage(CreateUserRequest request) {
        template.convertAndSend(userCreationExchange, userCreationRoutingKey, request);
    }

    public void publishUserImage(ImageUploadRequest request) {
        template.convertAndSend(userImageExchange, userImageRoutingKey, request);
    }
}