package com.smart.tech.start.management.service;

import com.smart.tech.start.management.entity.AccountEntity;
import com.smart.tech.start.management.repository.AccountRepository;
import com.smart.tech.start.management.request.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountRegistrationService {

    private final AccountRepository bankAccountRepository;

    public void register(RegistrationRequest request) {
        bankAccountRepository.save(new AccountEntity(request.getCurrencyCode()));
    }
}
