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
    @Value("${user.queue}")
    private String userQueue;

    @Value("${user.validation.exchange}")
    private String emailValidationExchange;

    @Value("${user.validation.key}")
    private String emailValidationRoutingKey;

    @Value("${user.deletion.exchange}")
    private String userDeletionExchange;

    @Value("${user.deletion.key}")
    private String userDeletionRoutingKey;

    @Bean
    public Queue queue() {
        return new Queue(userQueue);
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
    public Binding emailValidationBinding(Queue queue, TopicExchange emailValidationExchange) {
        return BindingBuilder.bind(queue).to(emailValidationExchange).with(emailValidationRoutingKey);
    }

    @Bean
    public Binding userDeletionBinding(Queue queue, TopicExchange userDeletionExchange) {
        return BindingBuilder.bind(queue).to(userDeletionExchange).with(userDeletionRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}