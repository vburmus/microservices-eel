package com.epam.esm.utils.amqp;

import com.epam.esm.certificate.service.CertificateService;
import com.epam.esm.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final CertificateService certificateService;
    private final TagService tagService;

    @RabbitListener(queues = "${tag.image.response.queue}")
    public void onTagImageUploaded(ImageUploadResponse response) {
        tagService.setUploadedImage(response);
    }

    @RabbitListener(queues = "${certificate.image.response.queue}")
    public void onCertificateImageUploaded(ImageUploadResponse response) {
        certificateService.setUploadedImage(response);
    }
}