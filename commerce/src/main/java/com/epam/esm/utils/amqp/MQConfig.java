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
    private String purchaseRoutingKey;
    @Value("${certificate.image.queue}")
    private String certificateImageQueue;
    @Value("${certificate.image.exchange}")
    private String certificateImageExchange;
    @Value("${certificate.image.key}")
    private String certificateImageRoutingKey;
    @Value("${tag.image.queue}")
    private String tagImageQueue;
    @Value("${tag.image.exchange}")
    private String tagImageExchange;
    @Value("${tag.image.key}")
    private String tagImageRoutingKey;

    @Bean
    public Queue purchaseQueue() {
        return new Queue(purchaseQueue);
    }

    @Bean
    public Queue certificateImageQueue() {
        return new Queue(certificateImageQueue);
    }

    @Bean
    public Queue tagImageQueue() {
        return new Queue(tagImageQueue);
    }

    @Bean
    public TopicExchange purchaseExchange() {
        return new TopicExchange(purchaseExchange);
    }

    @Bean
    public TopicExchange certificateImageExchange() {
        return new TopicExchange(certificateImageExchange);
    }

    @Bean
    public TopicExchange tagImageExchange() {
        return new TopicExchange(tagImageExchange);
    }

    @Bean
    public Binding purchaseBinding(Queue purchaseQueue, TopicExchange purchaseExchange) {
        return BindingBuilder.bind(purchaseQueue).to(purchaseExchange).with(purchaseRoutingKey);
    }

    @Bean
    public Binding certificateImageBinding(Queue certificateImageQueue, TopicExchange certificateImageExchange) {
        return BindingBuilder.bind(certificateImageQueue).to(certificateImageExchange).with(certificateImageRoutingKey);
    }

    @Bean
    public Binding tagImageBinding(Queue tagImageQueue, TopicExchange tagImageExchange) {
        return BindingBuilder.bind(tagImageQueue).to(tagImageExchange).with(tagImageRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}