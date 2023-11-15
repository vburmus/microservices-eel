package com.epam.esm.utils.ampq;

import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageListener {
    private final UserService userService;
    @RabbitListener(queues = "${user.queue}")
    public void onUserDeletion(String email) {
        userService.delete(email);
    }
}