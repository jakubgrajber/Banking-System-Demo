package com.smart.tech.start.domain.account;

import com.smart.tech.start.domain.service.CurrencyRatesService;
import com.smart.tech.start.domain.service.CurrencyRatesServiceExchangerateHostImpl;
import com.smart.tech.start.domain.utilities.Money;

import java.math.BigDecimal;
import java.util.Currency;

public class CheckingBankAccount implements BankAccount {

    private final CurrencyRatesService ratesService;
    private Money balance;
    private Currency currency;

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
    public void sendTransfer(Money money, BankAccount recipient) {
        if (recipient == null)
            throw new IllegalArgumentException("Cannot perform this operation - invalid recipient.");
        if (money.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Cannot perform this operation - invalid amount.");

        Money moneyToChargeFromSenderAccount;
        if (!money.isSameCurrencyAs(this.balance)){
            moneyToChargeFromSenderAccount = ratesService.exchange(money, this.currency);
        } else moneyToChargeFromSenderAccount = money;
        if (balance.compareTo(moneyToChargeFromSenderAccount) < 0)
            throw new IllegalArgumentException("Cannot perform this operation - not sufficient funds.");

        System.out.println(moneyToChargeFromSenderAccount);

        balance = balance.subtract(moneyToChargeFromSenderAccount);
        recipient.receiveTransfer(money, this);
    }

    @Override
    public void receiveTransfer(Money money, BankAccount sender) {
        if (!money.isSameCurrencyAs(balance)) {
            Money exchangedMoney = ratesService.exchange(money, this.currency);
            balance = balance.add(exchangedMoney);
        } else {
            balance = balance.add(money);
        }
    }

    @Override
    public boolean isBalanceEmpty() {
        return balance.isZero();
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

    @Override
    public String toString() {
        return "CheckingBankAccount{" +
                "balance=" + balance +
                '}';
    }
}
