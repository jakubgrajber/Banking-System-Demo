package com.smart.tech.start.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BankAccountRemovalRequest {
    private String userEmail;
    private UUID accountNumber;
}
