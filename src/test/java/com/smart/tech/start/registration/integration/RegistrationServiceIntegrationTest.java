package com.smart.tech.start.registration.integration;

import com.smart.tech.start.registration.registration.RegistrationRequest;
import com.smart.tech.start.registration.registration.RegistrationService;
import com.smart.tech.start.registration.token.ConfirmationToken;
import com.smart.tech.start.registration.token.ConfirmationTokenRepository;
import com.smart.tech.start.registration.token.ConfirmationTokenService;
import com.smart.tech.start.registration.user.User;
import com.smart.tech.start.registration.user.UserRole;
import com.smart.tech.start.registration.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RegistrationServiceIntegrationTest {

    private final static String FIRSTNAME = "James";
    private final static String LASTNAME = "Hetfield";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "j.h@example.com";
    private final static String EMAIL_NOT_VALID = "notValid.com";

    @Autowired
    RegistrationService registrationService;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    ConfirmationTokenService confirmationTokenService;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Check if throws IllegalStateException when the email is not valid")
    void shouldThrowIllegalStateException_WhenEmailIsNotValid() {
        // given
        RegistrationRequest request = new RegistrationRequest(FIRSTNAME, LASTNAME, PASSWORD, EMAIL_NOT_VALID);

        // when - then
        var exc = assertThrows(IllegalStateException.class, () -> registrationService.register(request));
        assertThat(exc.getMessage(), is("Email not valid"));
    }

    @Test
    @DisplayName("Check if throws IllegalStateException when the token is not found")
    void shouldThrowIllegalStateException_WhenThereIsNoSuchAToken() {

        // given
        String token = "invalid-token-value";

        // when - then
        var exc = assertThrows(IllegalStateException.class, () -> registrationService.confirmToken(token));
        assertThat(exc.getMessage(), is("token not found"));
    }

    @Test
    @DisplayName("Check if throws IllegalStateException when the token has already been used")
    void shouldThrowIllegalStateException_WhenTokenHasAlreadyBeenUsed(){

        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        String token = userService.signUpUser(user);

        confirmationTokenService.setConfirmedAt(token);

        // when - then
        var exc = assertThrows(IllegalStateException.class, () -> registrationService.confirmToken(token));
        assertThat(exc.getMessage(), is("email already confirmed"));
    }

    @Test
    @DisplayName("Check if throws IllegalStateException when the token expires")
    void shouldThrowIllegalStateException_WhenTokenExpires() {

        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        String token = userService.signUpUser(user);

        confirmationTokenService.setExpiresAt(token, LocalDateTime.now().plusMinutes(-15));


        // when - then
        var exc = assertThrows(IllegalStateException.class, () -> registrationService.confirmToken(token));
        assertThat(exc.getMessage(), is("token expired"));
    }

    @Test
    @DisplayName("Check if confirmation date is set when confirming token")
    void shouldSetConfirmedAt_WhenConfirmingToken(){

        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        String token = userService.signUpUser(user);

        // when
        registrationService.confirmToken(token);

        // then
        assertFalse(confirmationTokenRepository.findByToken(token).map(ConfirmationToken::getConfirmedAt).isEmpty());
    }

    @Test
    @DisplayName("Check if user  is enabled when confirming token")
    void shouldEnabledUser_WhenConfirmingToken(){

        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        String token = userService.signUpUser(user);

        // when
        registrationService.confirmToken(token);

        // then
        assertTrue(userService.loadUserByUsername(EMAIL).isEnabled());
    }

}