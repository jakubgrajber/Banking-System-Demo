package com.smart.tech.start.domain.account;

import com.smart.tech.start.domain.service.CurrencyRatesService;
import com.smart.tech.start.domain.service.CurrencyRatesServiceExchangerateHostImpl;
import com.smart.tech.start.domain.service.CurrencyRatesServiceResponse;
import com.smart.tech.start.domain.utilities.Money;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * This implementation of the BankAccount interface imitates checking bank account.
 * <p>
 * The purpose of the CheckingBankAccount domain model is to control flow of deposits and withdrawals.
 * <p>
 * It has the main currency in which money is stored.
 * Transactions in main currency are made immediately,
 * but it also allows you to make payments and receive funds in other currencies.
 * Then the money is exchanged according to the current rates.
 */

public class CheckingBankAccount implements BankAccount {

    private final CurrencyRatesService ratesService;
    private Money balance;
    private Currency currency;
    private BigDecimal transactionRate = BigDecimal.ONE;

    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("PLN");

    public CheckingBankAccount() {
        ratesService = new CurrencyRatesServiceExchangerateHostImpl();
    }

    public CheckingBankAccount(CurrencyRatesService ratesService) {
        this.ratesService = ratesService;
        this.currency = DEFAULT_CURRENCY;
        balance = new Money(BigDecimal.ZERO, this.currency);
    }

    public CheckingBankAccount(CurrencyRatesService ratesService, Currency currency) {
        this.ratesService = ratesService;
        this.currency = currency;
        balance = new Money(BigDecimal.ZERO, currency);
    }

    @Override
    public void send(Money money, BankAccount recipient) {
        if (recipient == null)
            throw new IllegalArgumentException("Cannot perform this operation - invalid recipient.");
        if (money.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Cannot perform this operation - invalid amount.");

        Money moneyToChargeFromSenderAccount;
        if (!money.isSameCurrencyAs(this.balance)) {
            CurrencyRatesServiceResponse ratesServiceResponse = ratesService.exchange(money, this.currency);
            moneyToChargeFromSenderAccount = ratesServiceResponse.getMoney();
            setTransactionRate(ratesServiceResponse.getRate());
        } else moneyToChargeFromSenderAccount = money;
        if (balance.compareTo(moneyToChargeFromSenderAccount) < 0)
            throw new IllegalArgumentException("Cannot perform this operation - not sufficient funds.");

        balance = balance.subtract(moneyToChargeFromSenderAccount);
        recipient.receive(money, this);
    }

    public void receive(Money money, BankAccount sender) {
        if (!money.isSameCurrencyAs(balance)) {
            CurrencyRatesServiceResponse ratesServiceResponse  = ratesService.exchange(money, this.currency);
            Money exchangedMoney = ratesServiceResponse.getMoney();
            setTransactionRate(ratesServiceResponse.getRate());
            balance = balance.add(exchangedMoney);
        } else {
            balance = balance.add(money);
        }
    }

    public Money getBalance() {
        return this.balance;
    }

    public void setBalance(Money accountBalance) {
        this.balance = accountBalance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getTransactionRate() {
        return transactionRate;
    }

    public void setTransactionRate(BigDecimal transactionRate) {
        this.transactionRate = transactionRate;
    }

    @Override
    public String toString() {
        return "CheckingBankAccount{" +
                "balance=" + balance +
                '}';
    }
}
