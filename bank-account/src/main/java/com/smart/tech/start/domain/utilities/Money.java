package com.smart.tech.start.domain.utilities;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public class Money implements Comparable<Money>, Serializable {

    public static final class MismatchedCurrencyException extends RuntimeException {
        MismatchedCurrencyException(String message) {
            super(message);
        }
    }

    private BigDecimal amount;
    private Currency currency;

    private static Currency DEFAULT_CURRENCY = Currency.getInstance("PLN");

    public Money(BigDecimal amount) {
        this.amount = amount;
        this.currency = DEFAULT_CURRENCY;
    }

    public Money(Money that){
        this.amount = that.amount;
        this.currency = that.currency;
    }

    public Money(BigDecimal amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isSameCurrencyAs(Money that) {
        boolean result = false;
        if (that != null) {
            result = this.currency.equals(that.currency);
        }
        return result;
    }

    public Money add(Money that) {
        checkCurrenciesMatch(that);
        return new Money(amount.add(that.amount), currency);
    }

    public Money subtract(Money that){
        checkCurrenciesMatch(that);
        return new Money(amount.subtract(that.amount), currency);
    }

    public Money div(BigDecimal that){
        return new Money(amount.divide(that,8, RoundingMode.HALF_UP), currency);
    }

    public boolean isZero(){
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public int compareTo(@NotNull Money that) {
        checkCurrenciesMatch(that);
        return this.amount.compareTo(that.amount);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Money thatMoney = (Money) that;
        checkCurrenciesMatch(thatMoney);

        return amount.equals(thatMoney.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    private void checkCurrenciesMatch(Money that){
        if (! this.currency.equals(that.getCurrency())) {
            throw new MismatchedCurrencyException(
                    "This operation cannot be performed in two different currencies.");
        }
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency=" + currency +
                '}';
    }
}
