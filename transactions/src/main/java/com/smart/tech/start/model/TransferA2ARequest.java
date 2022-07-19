package com.smart.tech.start.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TransferA2ARequest {
    private UUID senderAccountNumber;
    private UUID recipientAccountNumber;
    private BigDecimal amount;
    private String currencyCode;
    private String title;
}
