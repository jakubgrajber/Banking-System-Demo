package com.smart.tech.start.user.account.management.registration.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {

    private String firstname;
    private String lastname;
    private String password;
    private String email;

}
