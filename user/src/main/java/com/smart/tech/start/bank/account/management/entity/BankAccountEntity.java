package com.smart.tech.start.bank.account.management.entity;

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
public class BankAccountEntity {

    @Id
    @GeneratedValue
    private UUID accountNumber;

    @Column
    private BigDecimal balance;

    @Column
    private String currencyCode;
}
