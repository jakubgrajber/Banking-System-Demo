package com.smart.tech.start.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferA2ARepository extends JpaRepository<TransferA2AEntity, Long> {
}
