package domain;

import com.smart.tech.start.domain.account.BankAccount;
import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.domain.service.CurrencyRatesServiceTestingImpl;
import com.smart.tech.start.domain.utilities.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;


public class CheckingBankAccountTest {

    public static final Currency DEFAULT_CURRENCY = Currency.getInstance("PLN");
    public static final BigDecimal PLN_TO_EUR = new BigDecimal("0.21");
    public static final Money MONEY_IN_ANOTHER_CURRENCY = new Money(new BigDecimal("123.45"), Currency.getInstance("EUR"));
    public static final Money NEGATIVE_MONEY_AMOUNT = new Money(new BigDecimal(-1), DEFAULT_CURRENCY);
    public static final Money ZERO_MONEY_AMOUNT = new Money(new BigDecimal(BigInteger.ZERO), DEFAULT_CURRENCY);
    public static final Money ACCOUNT_BALANCE = new Money(new BigDecimal(5000), DEFAULT_CURRENCY);
    public static final Money BALANCE_AFTER_TRANSFER_IN_ANOTHER_CURRENCY = new Money(ACCOUNT_BALANCE.subtract(
                            new Money(MONEY_IN_ANOTHER_CURRENCY.div(PLN_TO_EUR).getAmount(), DEFAULT_CURRENCY)).getAmount(), DEFAULT_CURRENCY);
    public static final Money GREATER_THAN_ACCOUNT_BALANCE = new Money(new BigDecimal(5001), DEFAULT_CURRENCY);
    public static final Money MONEY_TO_TRANSFER = new Money(new BigDecimal("123.45"), DEFAULT_CURRENCY);
    public static final Money EUR_ACCOUNT_BALANCE_AFTER_PLN_TRANSFER = new Money(MONEY_TO_TRANSFER.getAmount().multiply(PLN_TO_EUR));
    public static final Money ACCOUNT_BALANCE_AFTER_TRANSFER = ACCOUNT_BALANCE.subtract(MONEY_TO_TRANSFER);

    private CheckingBankAccount senderAccount;
    private CheckingBankAccount recipientAccount;

    @BeforeEach
    void setUp() {
        senderAccount = new CheckingBankAccount(new CurrencyRatesServiceTestingImpl());
        recipientAccount = new CheckingBankAccount(new CurrencyRatesServiceTestingImpl());
    }

    @Test
    @DisplayName("New created account should have balance equals to zero")
    public void shouldHaveBalanceOfZero() {
        // GIVEN

        // THEN
        assertTrue(senderAccount.getBalance().equals(ZERO_MONEY_AMOUNT));
    }

    @Test
    @DisplayName("Account cannot transfer more money than balance amount")
    public void shouldThrowIllegalArgumentException_WhenTransferMoreMoneyThanBalanceAmount() {
        // GIVEN
        senderAccount.setBalance(ACCOUNT_BALANCE);

        // WHEN THEN
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> senderAccount.send(GREATER_THAN_ACCOUNT_BALANCE, recipientAccount));
        assertEquals("Cannot perform this operation - not sufficient funds.", throwable.getMessage());
    }

    @Test
    @DisplayName("The transfer should charge money from the account")
    public void shouldChargeMoney_WhenTransferIsPerformed() {
        // GIVEN
        senderAccount.setBalance(ACCOUNT_BALANCE);

        // WHEN
        senderAccount.send(MONEY_TO_TRANSFER, recipientAccount);

        // THEN
        assertTrue(senderAccount.getBalance().equals(ACCOUNT_BALANCE_AFTER_TRANSFER));
    }

    @Test
    @DisplayName("The transfer should not be performed when recipient is null")
    public void shouldThrowIllegalArgumentException_WhenRecipientIsNull() {
        // GIVEN
        senderAccount.setBalance(ACCOUNT_BALANCE);
        BankAccount nullAccount = null;

        // WHEN THEN
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> senderAccount.send(MONEY_TO_TRANSFER, nullAccount));
        assertEquals("Cannot perform this operation - invalid recipient.", throwable.getMessage());
    }

    @Test
    @DisplayName("The transfer should increase balance of the recipient account")
    public void shouldIncreaseRecipientAccountBalance_WhenTransferIsPerformed() {
        // GIVEN
        senderAccount.setBalance(ACCOUNT_BALANCE);

        // WHEN
        senderAccount.send(MONEY_TO_TRANSFER, recipientAccount);

        // THEN
        assertTrue(recipientAccount.getBalance().equals(MONEY_TO_TRANSFER));
    }

    @Test
    @DisplayName("The transfer should not be performed when it's equal to zero")
    public void shouldThrowIllegalArgumentException_WhenTransferringMoneyIsEqualToZero() {
        // GIVEN

        // WHEN THEN
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> senderAccount.send(ZERO_MONEY_AMOUNT, recipientAccount));
        assertEquals("Cannot perform this operation - invalid amount.", throwable.getMessage());
    }

    @Test
    @DisplayName("The transfer should not be performed when value is less than zero")
    public void shouldThrowIllegalArgumentException_WhenTransferringMoneyIsLessThenZero() {
        // GIVEN

        // WHEN THEN
        Throwable throwable = assertThrows(IllegalArgumentException.class, () -> senderAccount.send(NEGATIVE_MONEY_AMOUNT, recipientAccount));
        assertEquals("Cannot perform this operation - invalid amount.", throwable.getMessage());
    }

    @Test
    @DisplayName("The transfer with different currencies should increase balance of the recipient account")
    public void shouldIncreaseRecipientAccountBalance_WhenTransferWithDifferentCurrencyIsPerformed() {
        // GIVEN
        senderAccount.setBalance(ACCOUNT_BALANCE);
        CheckingBankAccount eurAccount = new CheckingBankAccount(new CurrencyRatesServiceTestingImpl(), Currency.getInstance("EUR"));

        // WHEN
        senderAccount.send(MONEY_TO_TRANSFER, eurAccount);

        // THEN
        assertEquals(EUR_ACCOUNT_BALANCE_AFTER_PLN_TRANSFER.getAmount(), eurAccount.getBalance().getAmount());
    }

    @Test
    @DisplayName("Should be able to transfer money in other currencies")
    public void shouldBeAbleToTransferMoneyInOtherCurrencies() {
        // GIVEN
        senderAccount.setBalance(ACCOUNT_BALANCE);
        CheckingBankAccount eurRecipientAccount = new CheckingBankAccount(new CurrencyRatesServiceTestingImpl(), Currency.getInstance("EUR"));

        // WHEN
        senderAccount.send(MONEY_IN_ANOTHER_CURRENCY, eurRecipientAccount);

        // THEN
        assertEquals(BALANCE_AFTER_TRANSFER_IN_ANOTHER_CURRENCY.getAmount(), senderAccount.getBalance().getAmount());
        assertEquals(MONEY_IN_ANOTHER_CURRENCY.getAmount(), eurRecipientAccount.getBalance().getAmount());
    }

}
