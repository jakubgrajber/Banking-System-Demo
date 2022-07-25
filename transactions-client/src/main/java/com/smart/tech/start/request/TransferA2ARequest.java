package com.smart.tech.start.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TransferA2ARequest {
    private String senderAccountNumber;
    private String senderCurrencyCode;
    private BigDecimal senderCurrencyExchangeRate;
    private String recipientAccountNumber;
    private String recipientCurrencyCode;
    private BigDecimal recipientCurrencyExchangeRate;
    private BigDecimal amount;
    private String transferCurrencyCode;
    private String title;
    private TransactionStatus transactionStatus;
    private String statusDescription;
}
