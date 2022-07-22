package com.smart.tech.start.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BankAccountRegistrationRequest {
    private String userEmail;
    private String currencyCode;
}
