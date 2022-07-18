package com.smart.tech.start.management.controller;

import com.smart.tech.start.management.request.RegistrationRequest;
import com.smart.tech.start.management.service.AccountRegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountRegistrationService registrationService;

    @PostMapping
    public String registerNewAccount(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return "done";
    }
}
