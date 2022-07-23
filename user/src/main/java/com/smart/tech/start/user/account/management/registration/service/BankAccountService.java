package com.smart.tech.start.user.account.management.registration.service;

import com.smart.tech.start.user.account.management.registration.entity.BankAccountEntity;
import com.smart.tech.start.user.account.management.registration.repository.BankAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class BankAccountService {

    private BankAccountRepository bankAccountRepository;

    public void save(BankAccountEntity account) {
        bankAccountRepository.save(account);
    }
}
