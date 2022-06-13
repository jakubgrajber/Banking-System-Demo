package com.smart.tech.start;


import com.smart.tech.start.entities.account.Account;
import com.smart.tech.start.entities.client.Client;
import com.smart.tech.start.entities.client.Person;
import com.smart.tech.start.entities.account.PersonalAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Account account;
    private Person client1;

    private static final int ZERO = 0;
    private static final int DEPOSIT_VALUE = 100;
    private static final int BIGGER_DEPOSIT_VALUE = 10000;
    private static final int MORE_THAN_NOMINAL_TRANSACTION_LIMIT = 1501;
    private static final int BAD_DEPOSIT_VALUE = -100;
    private static final int WITHDRAW_VALUE = 100;
    private static final int TRANSACTION_LIMIT = 5000;
    private static final int MORE_THAN_TRANSACTION_LIMIT = 6000;
    private static final Client SECOND_CLIENT = new Person("example", "example");


    @BeforeEach
    public void setup() {
        client1 = new Person("X", "Y");
        account = new PersonalAccount(client1);
    }

    @Test
    @DisplayName("Test if new created account has a balance of zero")
    public void newAccountShouldHaveBalanceEqualZero() {
        assertEquals(ZERO, account.getBalance(), "Balance is not equal zero");
    }

    @Test
    @DisplayName("Test if new created account after one deposit has a balance of that deposit")
    public void newAccountAfterOneDepositShouldHaveBalanceOfThatDeposit() {
        account.deposit(DEPOSIT_VALUE);
        assertEquals(DEPOSIT_VALUE, account.getBalance(), "Balance is not equal to the deposit");
    }

    @Test
    @DisplayName("Test if it's illegal to deposit zero or less")
    public void shouldThrowIllegalArgumentException_WhenDepositValueIsNotGreaterThanZero() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(BAD_DEPOSIT_VALUE));
    }

    @Test
    @DisplayName("Test if it's illegal to withdraw more money than is on account")
    public void shouldThrowIllegalStateException_WhenWithdrawMoneyOnNewCreatedAccount() {
        assertThrows(IllegalStateException.class, () -> account.withdraw(WITHDRAW_VALUE));
    }

    @Test
    @DisplayName("Test if balance is the same after deposit and withdraw the same amount of money")
    public void balanceShouldBeTheSame_WhenDepositXAndWithDrawX() {
        int balanceBefore = account.getBalance();
        account.deposit(DEPOSIT_VALUE);
        account.withdraw(DEPOSIT_VALUE);
        int balanceAfter = account.getBalance();

        assertEquals(balanceBefore, balanceAfter);
    }

    @Test
    @DisplayName("Test if new created account have transaction limit set to 1500")
    public void newCreatedAccountShouldHaveTransactionLimitSetTo1500() {
        account.deposit(BIGGER_DEPOSIT_VALUE);
        assertThrows(IllegalStateException.class, () -> account.withdraw(MORE_THAN_NOMINAL_TRANSACTION_LIMIT));
    }

    @Test
    @DisplayName("Test if it's illegal to withdraw more money than limit accept")
    public void shouldThrowIllegalStateException_WhenWithdrawMoreMoneyThanLimit() {
        account.setTransactionLimit(TRANSACTION_LIMIT);
        account.deposit(10000);
        assertThrows(IllegalStateException.class, () -> account.withdraw(MORE_THAN_TRANSACTION_LIMIT));
    }

    @Test
    @DisplayName("Test if can add another client to the account")
    public void canAddAnotherClientToTheAccount(){
        account.addClient(SECOND_CLIENT);
        assertEquals(2, account.getClients().size());
    }

    @Test
    @DisplayName("Test if can remove client from the account")
    public void canRemoveClientFromAccount() {
        account.addClient(SECOND_CLIENT);
        account.remove(SECOND_CLIENT);
        assertFalse(account.getClients().contains(SECOND_CLIENT));
    }

    @Test
    @DisplayName("Test if it's illegal to remove last client of an account")
    public void shouldThrowIllegalStateException_WhenRemoveOnlyOneClientOfAnAccount() {
        assertThrows(IllegalStateException.class, () -> account.remove(client1));
    }

    @Test
    public void shouldThrowIllegalStateException_WhenAddAnotherClientAndThenRemoveBoth(){
        account.addClient(SECOND_CLIENT);
        account.remove(SECOND_CLIENT);
        assertThrows(IllegalStateException.class, () -> account.remove(client1));
    }
}