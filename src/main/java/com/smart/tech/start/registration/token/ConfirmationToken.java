package com.smart.tech.start.registration.token;

import com.smart.tech.start.registration.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * ConfirmationToken is an Entity used for confirmation of users registration process.
 * <p>
 * It is created when the user registers a new user account, then it is injected into
 * the verification link and sent to the user's email address.
 */

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "confirmation_tokens", indexes = {
        @Index(name = "tokens_stringValue", columnList = "token")
})
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
