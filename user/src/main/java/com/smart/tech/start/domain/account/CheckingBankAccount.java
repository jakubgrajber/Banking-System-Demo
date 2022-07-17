package com.smart.tech.start.domain.account;

import com.smart.tech.start.domain.service.CurrencyRatesService;
import com.smart.tech.start.domain.utilites.Money;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class CheckingBankAccount implements BankAccount {

    private CurrencyRatesService ratesService;
    private Money balance;
    private Currency currency;

    private static final Currency DEFAULT_CURRENCY = Currency.getInstance("PLN");

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

    public Money getBalance() {
        return this.balance;
    }

    @Override
    public void sendTransfer(Money money, BankAccount recipient) {
        if (recipient == null)
            throw new IllegalArgumentException("Cannot perform this operation - invalid recipient.");
        if (money.isZero())
            throw new IllegalArgumentException("Cannot perform this operation - invalid amount.");
        if (balance.compareTo(money) < 0)
            throw new IllegalArgumentException("Cannot perform this operation - not sufficient funds.");

        balance = balance.subtract(money);
        recipient.receiveTransfer(money, this);
    }

    @Override
    public void receiveTransfer(Money money, BankAccount sender) {
        if (!money.isSameCurrencyAs(balance)){
            Money exchangedMoney = ratesService.exchange(money, this.currency);
            balance = balance.add(exchangedMoney);
        }
        else {
            balance = balance.add(money);
        }
    }

    @Override
    public boolean isBalanceEmpty() {
        return balance.isZero();
    }

    public void setBalance(Money accountBalance) {
        this.balance = accountBalance;
    }
}
