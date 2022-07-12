package com.smart.tech.start.registration.event;

import com.smart.tech.start.registration.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserRegisteredEvent {

    private String email;
    private String firstName;
    private String lastName;
    private String link;
}
