package com.smart.tech.start.user.account.management.registration.controllers;

import com.smart.tech.start.user.account.management.registration.request.RegistrationRequest;
import com.smart.tech.start.user.account.management.registration.service.UserRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/registration")
public class UserRegistrationController {

    private final UserRegistrationService registrationService;

    @PostMapping
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "confirmation")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
