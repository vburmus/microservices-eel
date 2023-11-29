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
    @Value("${certificate.image.response.queue}")
    private String certificateImageResponseQueue;
    @Value("${certificate.image.response.exchange}")
    private String certificateImageResponseExchange;
    @Value("${certificate.image.response.key}")
    private String certificateImageResponseRoutingKey;
    @Value("${tag.image.response.queue}")
    private String tagImageResponseQueue;
    @Value("${tag.image.response.exchange}")
    private String tagImageResponseExchange;
    @Value("${tag.image.response.key}")
    private String tagImageResponseRoutingKey;

    @Bean
    public Queue userImageResponseQueue() {
        return new Queue(userImageResponseQueue);
    }

    @Bean
    public Queue certificateImageResponseQueue() {
        return new Queue(certificateImageResponseQueue);
    }

    @Bean
    public Queue tagImageResponseQueue() {
        return new Queue(tagImageResponseQueue);
    }

    @Bean
    public TopicExchange userImageUploadExchange() {
        return new TopicExchange(userImageResponseExchange);
    }

    @Bean
    public TopicExchange certificateImageUploadExchange() {
        return new TopicExchange(certificateImageResponseExchange);
    }

    @Bean
    public TopicExchange tagImageUploadExchange() {
        return new TopicExchange(tagImageResponseExchange);
    }

    @Bean
    public Binding userImageUploadBinding(Queue userImageResponseQueue, TopicExchange userImageUploadExchange) {
        return BindingBuilder.bind(userImageResponseQueue).to(userImageUploadExchange).with(userImageResponseRoutingKey);
    }
    @Bean
    public Binding certificateImageUploadBinding(Queue certificateImageResponseQueue, TopicExchange certificateImageUploadExchange) {
        return BindingBuilder.bind(certificateImageResponseQueue).to(certificateImageUploadExchange).with(certificateImageResponseRoutingKey);
    }
    @Bean
    public Binding tagImageUploadBinding(Queue tagImageResponseQueue, TopicExchange tagImageUploadExchange) {
        return BindingBuilder.bind(tagImageResponseQueue).to(tagImageUploadExchange).with(tagImageResponseRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}