package com.smart.tech.start.management.service;

import com.smart.tech.start.management.entity.AccountEntity;
import com.smart.tech.start.management.repository.AccountRepository;
import com.smart.tech.start.management.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void register(RegistrationRequest request) {
        accountRepository.save(new AccountEntity(request.getCurrencyCode()));
    }

    public void delete(UUID accountNumber) {
        accountRepository.deleteById(accountNumber);
    }

    public Optional<AccountEntity> findById(String senderAccountNumber) {
        return accountRepository.findById(UUID.fromString(senderAccountNumber));
    }
}
