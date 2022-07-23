package com.smart.tech.start.user.account.management.registration.repository;

import com.smart.tech.start.user.account.management.registration.entity.BankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountEntity, UUID> {
}
