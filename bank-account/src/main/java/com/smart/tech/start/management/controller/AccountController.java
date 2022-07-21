package com.smart.tech.start.management.controller;

import com.smart.tech.start.management.request.RegistrationRequest;
import com.smart.tech.start.management.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService registrationService;

    @PostMapping
    public void registerNewAccount(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
    }

    @DeleteMapping
    public void deleteAccount(@RequestParam UUID accountNumber) {
        registrationService.delete(accountNumber);
    }
}
