package com.smart.tech.start.registration.services;

import com.smart.tech.start.registration.models.RegistrationRequest;
import com.smart.tech.start.registration.models.User;
import com.smart.tech.start.registration.models.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    private final EmailValidator emailValidator;
    private final UserService userService;

    public String register(RegistrationRequest request) {

        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("Email not valid");
        }
        return userService.singUpUser(new User(
                request.getFirstname(),
                request.getLastname(),
                request.getPassword(),
                request.getEmail(),
                UserRole.USER
            )
        );
    }
}
