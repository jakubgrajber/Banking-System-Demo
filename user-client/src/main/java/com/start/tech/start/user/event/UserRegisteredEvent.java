package com.start.tech.start.user.event;

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
