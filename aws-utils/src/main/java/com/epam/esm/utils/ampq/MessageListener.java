package com.epam.esm.utils.ampq;

import com.epam.esm.service.AwsUtilsService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.epam.esm.utils.Constants.USERS;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final AwsUtilsService awsUtilsService;

    @RabbitListener(queues = "${user.image.queue}")
    public void onUserDeletion(ImageUploadRequest imageUploadRequest) {
        awsUtilsService.loadByteImage(imageUploadRequest, USERS);
    }
}