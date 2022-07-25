package com.smart.tech.start.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    private String transferCurrencyCode;

    @Column
    private String senderCurrencyCode;

    @Column
    private BigDecimal senderCurrencyExchangeRate;

    @Column(nullable = false)
    private UUID recipientAccountNumber;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime requestedAt;

    @Column
    private String status;

    @Column
    private String statusDescription;

    @Column
    private String recipientCurrencyCode;

    @Column
    private BigDecimal recipientCurrencyExchangeRate;

    public TransferA2AEntity(UUID senderAccountNumber, BigDecimal amount, String currencyCode, UUID recipientAccountNumber, String title, LocalDateTime requestedAt) {
        this.senderAccountNumber = senderAccountNumber;
        this.amount = amount;
        this.transferCurrencyCode = currencyCode;
        this.recipientAccountNumber = recipientAccountNumber;
        this.title = title;
        this.requestedAt = requestedAt;
    }
}
