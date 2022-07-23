package com.smart.tech.start.bank.account.management.client;

import com.smart.tech.start.request.BankAccountRegistrationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient("bank-account")
public interface AccountClient {

    @RequestMapping(method = RequestMethod.POST, value = "api/account")
    void createAccount(@RequestBody BankAccountRegistrationRequest request);

    @RequestMapping(method = RequestMethod.DELETE, value = "?accountNumber={id}")
    void removeAccount(@PathVariable("id") UUID bankAccountNumber);
}
