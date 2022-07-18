package com.smart.tech.start.domain;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    private static final BigDecimal POSITIVE_MONEY_AMOUNT = new BigDecimal("123.4");
    private static final String FIRSTNAME = "James";
    private static final String LASTNAME = "Hetfield";
    private static final String PASSWORD = "password";
    private static final String EMAIL = "j.h@example.com";

    private CheckingBankAccount account;

    @BeforeEach
    void setUp() {
        account = new CheckingBankAccount();
    }

    @Test
    @DisplayName("Disabled user cannot register a new account")
    public void shouldThrowIllegalStateException_WhenDisabledUserRegistersAccount() {
        // GIVEN
        User user = new User();

        // WHEN - THEN
        Exception exception = assertThrows(IllegalStateException.class, () -> user.addAccount(account));
        assertEquals("Cannot add an account for disabled User.", exception.getMessage());
    }

    @Test
    @DisplayName("Enabled user can register a new account")
    public void shouldBeAbleToRegisterAccount_WhenUserIsEnabled() {
        // GIVEN
        User user = new User();
        user.enable();

        // WHEN
        user.addAccount(account);

        // THEN
        assertEquals(1, user.getAccounts().size());
    }

    @Test
    @DisplayName("New user should be disabled")
    public void newUserShouldBeDisabled() {
        //GIVEN
        User user = new User();

        assertFalse(user.isEnabled());
    }

    @Test
    @DisplayName("New user can be enabled")
    public void shouldBeAbleToEnableUser_WhenUserIsDisabled() {
        // GIVEN
        User user = new User();

        // WHEN
        user.enable();

        // THEN
        assertTrue(user.isEnabled());
    }

    @Test
    @DisplayName("Enabled user cannot be enabled again")
    public void shouldThrowIllegalStateException_WhenEnablesEnabledUser() {
        // GIVEN
        User user = new User();
        user.enable();

        // WHEN THEN
        Exception exception = assertThrows(IllegalStateException.class, user::enable);
        assertEquals("The User has already been enabled.", exception.getMessage());
    }

    @Test
    @DisplayName("Enabled user can remove account when its balance equals zero")
    public void shouldBeAbleToRemoveAccount_WhenAccountBalanceEqualsZero() {
        // GIVEN
        User user = createEnabledUser();
        user.addAccount(account);

        // WHEN
        user.removeAccount(account);

        // THEN
        assertEquals(0, user.getAccounts().size());
    }

    @Test
    @DisplayName("Enabled user cannot to be enabled again")
    public void shouldThrowIllegalStateException_WhenRemoveAccountWithBalanceThatNotEqualsZero() {
        // GIVEN
        account.setBalance(POSITIVE_MONEY_AMOUNT);
        User user = createEnabledUser();
        user.addAccount(account);

        // WHEN THEN
        Exception exception = assertThrows(IllegalStateException.class, () -> user.removeAccount(account));
        assertEquals("Cannot remove account with non-zero balance.", exception.getMessage());
    }

    @Test
    @DisplayName("Disabled user cannot remove account")
    public void shouldThrowIllegalStateException_WhenDisabledUserRemovesAccount() {
        // GIVEN
        User user = createEnabledUser();
        user.addAccount(account);
        user.disable();

        // WHEN THEN
        Exception exception = assertThrows(IllegalStateException.class, () -> user.removeAccount(account));
        assertEquals("Cannot remove account - User is disabled.", exception.getMessage());
    }

    @Test
    @DisplayName("Enabled user cannot remove account which is not assign to him")
    public void shouldThrowIllegalStateException_WhenRemoveAccountWithoutAddingSuchOneBefore() {
        // GIVEN
        User user = createEnabledUser();

        // WHEN THEN
        Exception exception = assertThrows(IllegalStateException.class, () -> user.removeAccount(account));
        assertEquals("There is no such account assigned to this User.", exception.getMessage());
    }

    @Test
    @DisplayName("New created user can be disabled")
    public void shouldThrowIllegalStateException_WhenTryToDisableNewCreatedUser() {
        // GIVEN
        User user = new User();

        // WHEN THEN
        Exception exception = assertThrows(IllegalStateException.class, user::disable);
        assertEquals("The User has already been disabled.", exception.getMessage());
    }

    @Test
    @DisplayName("Enabled user can be disabled")
    public void shouldBeAbleToDisableEnabledUser() {
        // GIVEN
        User user = createEnabledUser();

        // WHEN
        user.disable();

        // THEN
        assertFalse(user.isEnabled());
    }

    @Test
    @DisplayName("User can be created with valid details")
    public void shouldCreateUserProvidingValidDetails() {
        // GIVEN
        User user = new User(FIRSTNAME, LASTNAME, EMAIL, PASSWORD);

        // THEN
        assertEquals(FIRSTNAME, user.getFirstName());
        assertEquals(LASTNAME, user.getLastName());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
    }

    @Test
    @DisplayName("User can change his password providing new valid one")
    public void shouldChangePassword_WhenNewOneIsValid() {
        // GIVEN
        User user = new User(FIRSTNAME, LASTNAME, EMAIL, PASSWORD);
        String newPassword = "new-password";

        // WHEN
        user.changePassword(newPassword);

        // THEN
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    @DisplayName("User can change his email providing new valid one")
    public void shouldChangeEmail_WhenNewOneIsValid() {
        // GIVEN
        User user = new User(FIRSTNAME, LASTNAME, EMAIL, PASSWORD);
        String newEmail = "new@email.com";

        // WHEN
        user.changeEmail(newEmail);

        // THEN
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    @DisplayName("User can change his first name providing new valid one")
    public void shouldChangeFirstName_WhenNewOneIsValid() {
        // GIVEN
        User user = new User(FIRSTNAME, LASTNAME, EMAIL, PASSWORD);
        String newFirstName = "Kirk";

        // WHEN
        user.setFirstName(newFirstName);

        // THEN
        assertEquals(newFirstName, user.getFirstName());
    }

    @Test
    @DisplayName("User can change his last name providing new valid one")
    public void shouldChangeLastName_WhenNewOneIsValid() {
        // GIVEN
        User user = new User(FIRSTNAME, LASTNAME, EMAIL, PASSWORD);
        String newLastName = "Hammett";

        // WHEN
        user.setLastName(newLastName);

        // THEN
        assertEquals(newLastName, user.getLastName());
    }


    private User createEnabledUser() {
        User user = new User();
        user.enable();
        return user;
    }

}
