package com.epam.esm.utils.ampq;

import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageListener {
    private final UserService userService;

    @RabbitListener(queues = "${user.deletion.queue}")
    public void onUserDeletion(String email) {
        userService.delete(email);
    }

    @RabbitListener(queues = "${user.creation.queue}")
    public void onUserCreation(CreateUserRequest request) {
        userService.create(request);
    }

    @RabbitListener(queues = "${user.image.response.queue}")
    public void onUserCreation(ImageUploadResponse response) {
        userService.setUploadedImage(response);
    }
}