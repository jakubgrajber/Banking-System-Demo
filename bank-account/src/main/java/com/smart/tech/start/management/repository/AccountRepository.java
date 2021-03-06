package com.smart.tech.start.management.repository;

import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<CheckingBankAccountEntity, UUID> {
    CheckingBankAccountEntity findByCreatedBy(String userEmail);
}
