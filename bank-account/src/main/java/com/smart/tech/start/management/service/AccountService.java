package com.smart.tech.start.management.service;

import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.repository.AccountRepository;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void register(BankAccountRegistrationRequest request) {
        accountRepository.save(new CheckingBankAccountEntity(request.getCurrencyCode()));
    }

    public void delete(UUID accountNumber) {
        accountRepository.deleteById(accountNumber);
    }

    public Optional<CheckingBankAccountEntity> findById(String senderAccountNumber) {
        return accountRepository.findById(UUID.fromString(senderAccountNumber));
    }

    public void updateBalance(CheckingBankAccountEntity senderAccountEntity) {
        accountRepository.save(senderAccountEntity);
    }
}
