package com.smart.tech.start.request;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TransferA2ARequest {
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private BigDecimal amount;
    private String currencyCode;
    private String title;
}
