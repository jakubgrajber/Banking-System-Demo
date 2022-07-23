package com.smart.tech.start.management.controller;

import com.smart.tech.start.management.client.UserClient;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService accountService;
    private final UserClient userClient;

    @PostMapping
    public void registerNewAccount(@RequestBody BankAccountRegistrationRequest request) {
        accountService.register(request);

        CheckingBankAccountEntity account = accountService.findByEmail(request.getUserEmail());

        userClient.updateUserWithNewAccountNumber(account.getAccountNumber(), request.getUserEmail());
    }

    @DeleteMapping
    public void deleteAccount(@RequestParam UUID accountNumber) {
        accountService.delete(accountNumber);
    }
}
