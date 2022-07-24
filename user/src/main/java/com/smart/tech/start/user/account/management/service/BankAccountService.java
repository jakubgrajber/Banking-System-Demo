package com.smart.tech.start.user.account.management.service;

import com.smart.tech.start.user.account.management.entity.BankAccountEntity;
import com.smart.tech.start.user.account.management.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    public void save(BankAccountEntity account) {
        bankAccountRepository.save(account);
    }

    public long getAccountOwnerId(UUID bankAccountNumber) {
        return bankAccountRepository.findById(bankAccountNumber).orElseThrow(
                        () -> new RuntimeException(String.format("The account number %s has not been found.", bankAccountNumber)))
                .getUser().getId();
    }

    public void delete(UUID bankAccountNumber) {
        bankAccountRepository.deleteById(bankAccountNumber);
    }
}
