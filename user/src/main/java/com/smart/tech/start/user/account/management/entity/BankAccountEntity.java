package com.smart.tech.start.user.account.management.entity;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountEntity {

    @Id
    private UUID accountNumber;

    @ManyToOne
    private UserEntity user;
}
