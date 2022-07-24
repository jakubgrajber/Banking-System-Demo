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
        accountRepository.save(new CheckingBankAccountEntity(request.getCurrencyCode(), request.getUserEmail()));
    }

    public void delete(UUID accountNumber) {
        accountRepository.deleteById(accountNumber);
    }

    public CheckingBankAccountEntity findById(String senderAccountNumber) {
        return accountRepository.findById(UUID.fromString(senderAccountNumber)).orElseThrow(
                () -> new RuntimeException(String.format("The account %s has not been found."))
        );
    }

    public void updateBalance(CheckingBankAccountEntity account) {
        accountRepository.save(account);
    }

    public CheckingBankAccountEntity findByEmail(String userEmail) {
        return accountRepository.findByCreatedBy(userEmail);
    }

    public boolean emailsMatch(String senderAccountNumber, String issuerEmail) {
        return findById(senderAccountNumber).getCreatedBy().equals(issuerEmail);
    }
}
