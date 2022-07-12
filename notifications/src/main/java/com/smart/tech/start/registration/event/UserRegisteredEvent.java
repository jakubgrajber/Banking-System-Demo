package com.smart.tech.start.registration.event;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserRegisteredEvent {

    private String email;
    private String firstName;
    private String lastName;
    private String link;
}
