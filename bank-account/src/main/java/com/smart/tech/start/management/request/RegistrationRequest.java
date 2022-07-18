package com.smart.tech.start.management.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String userEmail;
    private String currencyCode;
}
