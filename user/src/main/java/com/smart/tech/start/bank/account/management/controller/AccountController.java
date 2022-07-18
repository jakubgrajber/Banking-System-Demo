package com.smart.tech.start.bank.account.management.controller;

import com.smart.tech.start.bank.account.management.request.AccountRegisterRequest;
import com.smart.tech.start.bank.account.management.service.AccountRegistrationService;
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
    public String addNewAccount(@RequestBody AccountRegisterRequest request) {
        return registrationService.register(request);
    }
}
