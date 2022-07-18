package com.smart.tech.start.bank.account.management;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BankAccountRegistrationRequest {
    private String userEmail;
    private String accountType;
    private String currencyCode;
}
