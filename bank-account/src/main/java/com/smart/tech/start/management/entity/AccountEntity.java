package com.smart.tech.start.management.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "bank_accounts")
public class AccountEntity {

    @Id
    @GeneratedValue
    private UUID accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currencyCode;

    public AccountEntity(String currencyCode) {
        this.currencyCode = currencyCode;
        this.balance = new BigDecimal(0);
    }
}
