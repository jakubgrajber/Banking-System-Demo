package com.smart.tech.start.bank.account.management;

import com.smart.tech.start.bank.account.management.client.AccountClient;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user/bank-account")
@AllArgsConstructor
public class BankAccountController {

    private AccountClient accountClient;

    @PostMapping
    public void createAccount(@RequestBody BankAccountRegistrationRequest request){

        accountClient.createAccount(request);
    }

    @DeleteMapping
    public void deleteAccount(@RequestParam UUID bankAccountNumber) {
        accountClient.removeAccount(bankAccountNumber);
    }


}
