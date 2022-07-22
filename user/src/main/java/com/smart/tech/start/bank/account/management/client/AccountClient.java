package com.smart.tech.start.bank.account.management.client;

import com.smart.tech.start.bank.account.management.BankAccountRegistrationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "bank-account", path = "api/account")
public interface AccountClient {

    @PostMapping
    void createAccount(@RequestBody BankAccountRegistrationRequest request);

    @RequestMapping(method = RequestMethod.DELETE, value = "?accountNumber={id}")
    void removeAccount(@PathVariable("id") UUID bankAccountNumber);
}
