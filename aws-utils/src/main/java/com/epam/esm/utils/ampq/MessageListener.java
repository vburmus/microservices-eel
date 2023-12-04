package com.epam.esm.utils.ampq;

import com.epam.esm.service.AwsUtilsService;
import com.epam.esm.service.Directory;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageListener {
    private final AwsUtilsService awsUtilsService;

    @RabbitListener(queues = "${user.image.queue}")
    public void onUserImageUpload(ImageUploadRequest imageUploadRequest) {
        awsUtilsService.loadByteImage(imageUploadRequest, Directory.USERS);
    }

    @RabbitListener(queues = "${certificate.image.queue}")
    public void onCertificateImageUpload(ImageUploadRequest imageUploadRequest) {
        awsUtilsService.loadByteImage(imageUploadRequest, Directory.CERTIFICATES);
    }

    @RabbitListener(queues = "${tag.image.queue}")
    public void onTagImageUpload(ImageUploadRequest imageUploadRequest) {
        awsUtilsService.loadByteImage(imageUploadRequest, Directory.TAGS);
    }
}