package com.smart.tech.start.user.account.management.service;

import com.smart.tech.start.user.account.management.entity.ConfirmationToken;
import com.smart.tech.start.user.account.management.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@AllArgsConstructor
@Transactional
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    private final Clock clock;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }

    public void setConfirmedAt(String token) {
        confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now(clock));
    }

    public void setExpiresAt(String token, LocalDateTime time) {
        confirmationTokenRepository.updateExpiresAt(token, time);
    }
}
