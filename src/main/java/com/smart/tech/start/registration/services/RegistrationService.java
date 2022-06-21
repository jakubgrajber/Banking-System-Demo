package com.smart.tech.start.registration.services;

import com.smart.tech.start.registration.models.RegistrationRequest;
import com.smart.tech.start.registration.models.User;
import com.smart.tech.start.registration.models.UserRole;
import com.smart.tech.start.registration.token.ConfirmationToken;
import com.smart.tech.start.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        String token = userService.singUpUser(new User(
                request.getFirstname(),
                request.getLastname(),
                request.getPassword(),
                request.getEmail(),
                UserRole.USER
            )
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;

        return token;
    }

    @Transactional
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
