package com.smart.tech.start.bank.account.management.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class BankAccountEntity {

    @Id
    @Type(type = "uuid-char")
    private UUID accountNumber;

    @Column
    private BigDecimal balance;

    @Column
    private String currencyCode;
}
