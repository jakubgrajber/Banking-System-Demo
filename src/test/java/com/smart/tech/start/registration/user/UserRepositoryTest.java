package com.smart.tech.start.registration.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private final static String FIRSTNAME = "James";
    private final static String LASTNAME = "Hetfield";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "j.h@example.com";

    @Test
    @DisplayName("Check if user can be found by his email")
    void shouldBeAbleToFindUserByHisEmail() {
        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        userRepository.save(user);

        // when
        Optional<User> optionalUser = userRepository.findByEmail(EMAIL);

        // then
        assertTrue(optionalUser.isPresent());
    }

    @Test
    @DisplayName("Check if user can be found by his email")
    void shouldReturnNullWhenNoOneUserHasSuchAnEmail() {
        // given

        // when
        Optional<User> optionalUser = userRepository.findByEmail(EMAIL);

        // then
        assertFalse(optionalUser.isPresent());
    }



}