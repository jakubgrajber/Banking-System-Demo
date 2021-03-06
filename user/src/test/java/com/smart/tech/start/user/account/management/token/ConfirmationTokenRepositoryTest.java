package com.smart.tech.start.user.account.management.token;

import com.smart.tech.start.UserApplication;
import com.smart.tech.start.user.account.management.entity.ConfirmationToken;
import com.smart.tech.start.user.account.management.entity.UserEntity;
import com.smart.tech.start.user.account.management.repository.ConfirmationTokenRepository;
import com.smart.tech.start.user.account.management.repository.UserRepository;
import com.smart.tech.start.user.account.management.utilites.UserRole;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UserApplication.class)
@Transactional
//@DataJpaTest
class ConfirmationTokenRepositoryTest {

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    UserRepository userRepository;

    private final static String FIRSTNAME = "James";
    private final static String LASTNAME = "Hetfield";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "j.h@example.com";
    private UserEntity USER = new UserEntity(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);

    private final static String TOKEN_STRING = "11b34543-b48a-417d-b330-1f13535530e2";
    private final static LocalDateTime CREATED_AT = LocalDateTime.now();
    private final static int TIME_AFTER_WHICH_TOKEN_EXPIRES_IN_MINUTES = 15;
    private final LocalDateTime EXPIRES_AT = CREATED_AT.plusMinutes(TIME_AFTER_WHICH_TOKEN_EXPIRES_IN_MINUTES);

    @Test
    @DisplayName("Check if token can be found by its value")
    void itShouldCheckIfTokenExists() {

        // given
        saveNewToken();

        // when
        Optional<ConfirmationToken> tokenOptional = confirmationTokenRepository.findByToken(TOKEN_STRING);

        // then
        assertTrue(tokenOptional.isPresent());
    }

    @Test
    @DisplayName("Check if token cannot be found by its value when it does not exist")
    void tokenShouldNotBeFoundWhenDoesNotExist() {

        // given

        // when
        Optional<ConfirmationToken> tokenOptional = confirmationTokenRepository.findByToken(TOKEN_STRING);

        // then
        assertFalse(tokenOptional.isPresent());
    }

    @Test
    @DisplayName("Check if token is updated with proper confirmation date")
    void itShouldCheckIfTokenIsUpdatedWithConfirmationDate() {
        // given
        saveNewToken();

        // when
        LocalDateTime confirmationTime = LocalDateTime.now();
        confirmationTokenRepository.updateConfirmedAt(TOKEN_STRING, confirmationTime);

        // then
        Optional<ConfirmationToken> tokenOptional = confirmationTokenRepository.findByToken(TOKEN_STRING);
        ConfirmationToken token = tokenOptional.orElse(new ConfirmationToken());

        assertEquals(confirmationTime, token.getConfirmedAt());
    }

    @Test
    @DisplayName("Check if token is updated with proper expiration date")
    void itShouldCheckIfTokenIsUpdatedWithExpirationDate() {

        // given
        saveNewToken();

        // when
        LocalDateTime confirmationTime = LocalDateTime.now();
        confirmationTokenRepository.updateExpiresAt(TOKEN_STRING, confirmationTime);

        // then
        Optional<ConfirmationToken> tokenOptional = confirmationTokenRepository.findByToken(TOKEN_STRING);
        ConfirmationToken token = tokenOptional.orElse(new ConfirmationToken());

        assertEquals(confirmationTime, token.getExpiresAt());
    }

    private void saveNewToken() {
        userRepository.save(USER);
        confirmationTokenRepository.save(new ConfirmationToken(TOKEN_STRING, CREATED_AT, EXPIRES_AT, USER));
    }
}