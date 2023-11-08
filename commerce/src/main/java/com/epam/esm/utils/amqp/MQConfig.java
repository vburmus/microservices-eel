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

@Configuration
@RequiredArgsConstructor
public class MQConfig {
    private final ObjectMapper objectMapper;
    @Value("${purchase.queue}")
    private String purchaseQueue;
    @Value("${purchase.exchange}")
    private String purchaseExchange;
    @Value("${purchase.key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(purchaseQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(purchaseExchange);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}