package com.smart.tech.start.management.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * CheckingBankAccountEntity is na entity that stores the account balance.
 *
 * After creating new one, the balance is zero.
 * For training purposes the accountNumber (identifier) is just a UUID.
 */

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "checking_bank_accounts")
public class CheckingBankAccountEntity {

    @Id
    @GeneratedValue
    private UUID accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private String createdBy;

    public CheckingBankAccountEntity(String currencyCode, String createdBy) {
        this.currencyCode = currencyCode;
        this.balance = new BigDecimal(BigInteger.ZERO);
        this.createdBy = createdBy;
    }
}
