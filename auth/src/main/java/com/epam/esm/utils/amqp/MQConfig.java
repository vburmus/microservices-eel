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
    @Value("${user.exchange}")
    private String userExchange;
    @Value("${user.key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(userQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(userExchange);
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