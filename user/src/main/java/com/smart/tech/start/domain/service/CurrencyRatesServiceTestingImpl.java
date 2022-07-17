package com.smart.tech.start.domain.service;

import com.smart.tech.start.domain.utilites.Money;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * This CurrencyRatesService implementation is just for testing purposes
 */

public class CurrencyRatesServiceTestingImpl implements CurrencyRatesService {

    private static final BigDecimal PLN_TO_EUR = new BigDecimal("0.21");

    @Override
    public Money exchange(Money from, Currency to) {
        BigDecimal newAmount = from.getAmount().multiply(PLN_TO_EUR);
        return new Money(newAmount, to);
    }
}
