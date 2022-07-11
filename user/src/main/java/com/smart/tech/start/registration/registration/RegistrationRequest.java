package com.smart.tech.start.registration.registration;

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
