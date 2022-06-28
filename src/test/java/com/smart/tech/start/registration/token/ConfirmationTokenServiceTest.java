package com.smart.tech.start.registration.token;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.*;

import static java.time.Instant.ofEpochMilli;
import static org.mockito.Mockito.verify;

class ConfirmationTokenServiceTest {

    @Mock
    ConfirmationTokenRepository confirmationTokenRepository;

    AutoCloseable autoCloseable;
    ConfirmationTokenService confirmationTokenService;

    private final static String TOKEN_VALUE = "UNIQUE-TOKEN-VALUE";
    private final Clock clock = Clock.fixed(ofEpochMilli(0), ZoneId.systemDefault());


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        confirmationTokenService = new ConfirmationTokenService(confirmationTokenRepository, clock);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canSaveConfirmationToken() {

        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();

        // when
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        // then
        verify(confirmationTokenRepository).save(confirmationToken);
    }

    @Test
    void canGetToken() {

        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();

        // when
        confirmationTokenService.getToken(TOKEN_VALUE);

        // then
        verify(confirmationTokenRepository).findByToken(TOKEN_VALUE);
    }

    @Test
    void canSetConfirmedAt() {

        // given
        ConfirmationToken confirmationToken = new ConfirmationToken();

        // when
        confirmationTokenService.setConfirmedAt(TOKEN_VALUE);

        // then
        verify(confirmationTokenRepository).updateConfirmedAt(TOKEN_VALUE, LocalDateTime.now(clock));
    }
}