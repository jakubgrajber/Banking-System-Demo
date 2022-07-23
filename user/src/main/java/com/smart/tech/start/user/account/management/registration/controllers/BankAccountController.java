package com.smart.tech.start.user.account.management.registration.controllers;

import com.smart.tech.start.user.account.management.registration.entity.BankAccountEntity;
import com.smart.tech.start.user.account.management.registration.entity.UserEntity;
import com.smart.tech.start.user.account.management.registration.service.BankAccountService;
import com.smart.tech.start.user.account.management.registration.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/bank-account")
@AllArgsConstructor
public class BankAccountController {

    final private BankAccountService bankAccountService;
    final private UserService userService;

    @PostMapping
    void registerNewAccount(@RequestParam UUID accountNumber, @RequestParam String userEmail){

        UserEntity user= userService.getUserByEmail(userEmail);

        bankAccountService.save(new BankAccountEntity(accountNumber, user));
    }
}
