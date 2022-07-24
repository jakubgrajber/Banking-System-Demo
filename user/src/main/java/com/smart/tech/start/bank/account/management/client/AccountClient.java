package com.smart.tech.start.bank.account.management.client;

import com.smart.tech.start.request.BankAccountRegistrationRequest;
import com.smart.tech.start.request.BankAccountRemovalRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "bank-account", path = "api/account")
public interface AccountClient {

    @RequestMapping(method = RequestMethod.POST)
    void createAccount(@RequestHeader(value = "Authorization") String header, @RequestBody BankAccountRegistrationRequest request);

    @RequestMapping(method = RequestMethod.DELETE)
    ResponseEntity<String> removeAccount(@RequestHeader(value = "Authorization") String header, @RequestBody BankAccountRemovalRequest request);
}
