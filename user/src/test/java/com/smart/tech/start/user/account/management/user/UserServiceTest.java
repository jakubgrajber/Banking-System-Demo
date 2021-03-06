package com.smart.tech.start.user.account.management.user;

import com.smart.tech.start.user.account.management.config.MailConfigProperties;
import com.smart.tech.start.user.account.management.entity.ConfirmationToken;
import com.smart.tech.start.user.account.management.entity.UserEntity;
import com.smart.tech.start.user.account.management.repository.UserRepository;
import com.smart.tech.start.user.account.management.service.UserService;
import com.smart.tech.start.user.account.management.service.ConfirmationTokenService;
import com.smart.tech.start.user.account.management.utilites.UserRole;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = PasswordEncoderTestContextConfig.class)
@SpringBootTest(classes = MailConfigProperties.class)
@EnableConfigurationProperties(MailConfigProperties.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailConfigProperties mailConfigProperties;

    private UserService userService;

    private AutoCloseable autoCloseable;

    private final static String FIRSTNAME = "James";
    private final static String LASTNAME = "Hetfield";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "j.h@example.com";


    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder, confirmationTokenService, mailConfigProperties);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("Check if user can be found by his username (email)")
    void canLoadUserByUsername() {
        // given
        UserEntity userToReturnFromRepository = new UserEntity(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userToReturnFromRepository));

        // when
        userService.loadUserByUsername(EMAIL);

        // then
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    @DisplayName("Check if user can be found by his username (email)")
    void shouldThrowUsernameNotFoundException_WhenThereIsNoUserWithSuchAUsername() {
        // when - then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }

    @Test
    @DisplayName("Check if user can be found by his email")
    void cnaGetUserByEmail() {
        // given
        UserEntity userToReturnFromRepository = new UserEntity(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userToReturnFromRepository));

        // when
        userService.loadUserByUsername(EMAIL);

        // then
        verify(userRepository).findByEmail(EMAIL);
    }

    @Test
    @DisplayName("Check if user can be found by his email")
    void shouldThrowUsernameNotFoundException_WhenThereIsNoUserWithSuchAnEmail() {
        // when - then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(EMAIL));
    }

    @Test
    @DisplayName("Check if method throws IllegalStateException when email is already taken")
    void shouldThrowIllegalStateException_whenSigningUpAUserWithAnAlreadyTakenEmail() {
        // given
        UserEntity userToReturnFromRepository = new UserEntity(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        UserEntity userToSignUp = new UserEntity("example", "example", "password", EMAIL, UserRole.USER);
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(userToReturnFromRepository));

        // when - then
        assertThrows(IllegalStateException.class, () -> userService.signUpUser(userToSignUp));

    }



    @Test
    @DisplayName("Check if user can be signed up")
    void canSignUpANewUserWithUniqueEmail(){
        //given
        UserEntity user = new UserEntity(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);

        // when
        userService.signUpUser(user);

        // then
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Check if confirmation token is created while signing up a new user")
    void canSigningUpANewUserSaveToken(){
        //given
        UserEntity user = new UserEntity(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);

        // when
        String token = userService.signUpUser(user);

        // then
        verify(confirmationTokenService).saveConfirmationToken(any(ConfirmationToken.class));
    }

    @Test
    @DisplayName("Check if user can be enabled")
    void canEnableUser() {

        // when
        userService.enableUser(EMAIL);

        // then
        verify(userRepository).enableUser(EMAIL);
    }
}