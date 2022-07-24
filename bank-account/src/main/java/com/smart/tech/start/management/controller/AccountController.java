package com.smart.tech.start.management.controller;

import com.smart.tech.start.management.client.UserClient;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService accountService;
    private final UserClient userClient;

    @PostMapping
    public void registerNewAccount(@RequestBody BankAccountRegistrationRequest bodyRequest, HttpServletRequest servletRequest) {
        String header = servletRequest.getHeader(AUTHORIZATION);

        accountService.register(bodyRequest);

        CheckingBankAccountEntity account = accountService.findByEmail(bodyRequest.getUserEmail());

        userClient.updateUserWithNewAccountNumber(header, account.getAccountNumber(), bodyRequest.getUserEmail());
    }

    @DeleteMapping
    public void deleteAccount(@RequestParam UUID accountNumber) {
        accountService.delete(accountNumber);
    }
}
