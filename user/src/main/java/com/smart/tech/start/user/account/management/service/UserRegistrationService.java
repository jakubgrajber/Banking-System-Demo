package com.smart.tech.start.user.account.management.service;

import com.smart.tech.start.user.account.management.entity.ConfirmationToken;
import com.smart.tech.start.user.account.management.entity.UserEntity;
import com.smart.tech.start.user.account.management.utilites.EmailValidator;
import com.smart.tech.start.user.account.management.request.RegistrationRequest;
import com.smart.tech.start.user.account.management.utilites.UserRole;
import com.start.tech.start.user.event.UserRegisteredEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Transactional
public class UserRegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail) {
            throw new IllegalStateException("Email not valid");
        }
        String token = userService.signUpUser(new UserEntity(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getPassword(),
                        request.getEmail(),
                        UserRole.USER
                )
        );

        String link = "http://localhost:8080/api/registration/confirmation?token=" + token;

        kafkaTemplate.send("users", request.getEmail(),
                new UserRegisteredEvent(
                        request.getEmail(),
                        request.getFirstname(),
                        request.getLastname(),
                        link
                ));

        return token;
    }

    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail());
        return "confirmed";
    }
}
