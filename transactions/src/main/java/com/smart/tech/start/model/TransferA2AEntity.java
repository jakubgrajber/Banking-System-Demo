package com.smart.tech.start.model;

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
@Table(name = "a2a_transfers")
public class TransferA2AEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private UUID senderAccountNumber;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currencyCode;

    @Column(nullable = false)
    private UUID recipientAccountNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String status;

    @Column
    private String recipientCurrencyCode;

    @Column
    private double currencyExchangeRate;


}
