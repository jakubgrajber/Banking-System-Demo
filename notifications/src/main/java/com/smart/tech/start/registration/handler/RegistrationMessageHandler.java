package com.smart.tech.start.registration.handler;

import com.smart.tech.start.registration.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RegistrationMessageHandler {

    @KafkaListener(topics = "users")
    public void onMessageHandle(@Payload UserRegisteredEvent event) {
        log.info("New event: {}", event);
    }
}
