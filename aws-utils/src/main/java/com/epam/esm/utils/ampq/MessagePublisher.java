package com.epam.esm.utils.ampq;

import com.epam.esm.service.Directory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessagePublisher {
    private final RabbitTemplate template;
    @Value("${user.image.response.exchange}")
    private String userImageExchange;
    @Value("${user.image.response.key}")
    private String userImageRoutingKey;
    @Value("${certificate.image.response.exchange}")
    private String certificateImageResponseExchange;
    @Value("${certificate.image.response.key}")
    private String certificateImageResponseRoutingKey;
    @Value("${tag.image.response.exchange}")
    private String tagImageResponseExchange;
    @Value("${tag.image.response.key}")
    private String tagImageResponseRoutingKey;

    public void publishLoadedImageResponse(ImageUploadResponse imageUploadResponse, Directory directory) {
        switch (directory) {
            case USERS -> template.convertAndSend(userImageExchange, userImageRoutingKey, imageUploadResponse);
            case CERTIFICATES ->
                    template.convertAndSend(certificateImageResponseExchange, certificateImageResponseRoutingKey,
                            imageUploadResponse);
            case TAGS ->
                    template.convertAndSend(tagImageResponseExchange, tagImageResponseRoutingKey, imageUploadResponse);
        }
    }
}