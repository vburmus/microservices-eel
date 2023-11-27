package com.epam.esm.utils.ampq;

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

    @Value("${user.image.response.queue}")
    private String userImageResponseQueue;
    @Value("${user.image.response.exchange}")
    private String userImageResponseExchange;
    @Value("${user.image.response.key}")
    private String userImageResponseRoutingKey;

    @Bean
    public Queue userImageQueue() {
        return new Queue(userImageResponseQueue);
    }

    @Bean
    public TopicExchange imageUploadExchange() {
        return new TopicExchange(userImageResponseExchange);
    }

    @Bean
    public Binding imageUploadBinding(Queue userImageQueue, TopicExchange imageUploadExchange) {
        return BindingBuilder.bind(userImageQueue).to(imageUploadExchange).with(userImageResponseRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}