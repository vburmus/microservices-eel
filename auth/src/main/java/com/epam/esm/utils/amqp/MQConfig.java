package com.epam.esm.utils.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@RequiredArgsConstructor
@Configuration
public class MQConfig {
    private final ObjectMapper objectMapper;

    @Value("${user.validation.queue}")
    private String userValidationQueue;
    @Value("${user.validation.exchange}")
    private String emailValidationExchange;
    @Value("${user.validation.key}")
    private String emailValidationRoutingKey;
    @Value("${user.deletion.queue}")
    private String userDeletionQueue;
    @Value("${user.deletion.exchange}")
    private String userDeletionExchange;
    @Value("${user.deletion.key}")
    private String userDeletionRoutingKey;
    @Value("${user.creation.queue}")
    private String userCreationQueue;
    @Value("${user.creation.exchange}")
    private String userCreationExchange;
    @Value("${user.creation.key}")
    private String userCreationRoutingKey;
    @Value("${user.image.queue}")
    private String userImageQueue;
    @Value("${user.image.exchange}")
    private String userImageExchange;
    @Value("${user.image.key}")
    private String userImageRoutingKey;

    @Bean
    public Queue userCreationQueue() {
        return new Queue(userCreationQueue);
    }

    @Bean
    public Queue userValidationQueue() {
        return new Queue(userValidationQueue);
    }

    @Bean
    public Queue userDeletionQueue() {
        return new Queue(userDeletionQueue);
    }

    @Bean
    public Queue userImageQueue() {
        return new Queue(userImageQueue);
    }


    @Bean
    public TopicExchange emailValidationExchange() {
        return new TopicExchange(emailValidationExchange);
    }

    @Bean
    public TopicExchange userDeletionExchange() {
        return new TopicExchange(userDeletionExchange);
    }

    @Bean
    public TopicExchange userCreationExchange() {
        return new TopicExchange(userCreationExchange);
    }

    @Bean
    public TopicExchange imageUploadExchange() {
        return new TopicExchange(userImageExchange);
    }

    @Bean
    public Binding emailValidationBinding(Queue userValidationQueue, TopicExchange emailValidationExchange) {
        return BindingBuilder.bind(userValidationQueue).to(emailValidationExchange).with(emailValidationRoutingKey);
    }

    @Bean
    public Binding userDeletionBinding(Queue userDeletionQueue, TopicExchange userDeletionExchange) {
        return BindingBuilder.bind(userDeletionQueue).to(userDeletionExchange).with(userDeletionRoutingKey);
    }

    @Bean
    public Binding userCreationBinding(Queue userCreationQueue, TopicExchange userCreationExchange) {
        return BindingBuilder.bind(userCreationQueue).to(userCreationExchange).with(userCreationRoutingKey);
    }

    @Bean
    public Binding imageUploadBinding(Queue userImageQueue, TopicExchange imageUploadExchange) {
        return BindingBuilder.bind(userImageQueue).to(imageUploadExchange).with(userImageRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}