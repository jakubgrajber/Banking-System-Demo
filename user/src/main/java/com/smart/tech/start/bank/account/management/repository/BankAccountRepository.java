package com.smart.tech.start.bank.account.management.repository;

import com.smart.tech.start.bank.account.management.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<UUID, BankAccountEntity> {
}
