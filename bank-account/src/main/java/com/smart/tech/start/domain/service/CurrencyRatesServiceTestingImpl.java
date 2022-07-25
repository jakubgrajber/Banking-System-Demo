package com.smart.tech.start.domain.service;


import com.smart.tech.start.domain.utilities.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * This CurrencyRatesService implementation is just for testing purposes
 */

public class CurrencyRatesServiceTestingImpl implements CurrencyRatesService {

    private static final BigDecimal PLN_TO_EUR = new BigDecimal("0.21");

    @Override
    public CurrencyRatesServiceResponse exchange(Money from, Currency to) {
        BigDecimal newAmount;
        if (from.getCurrency().getCurrencyCode().equals("PLN")) {
            newAmount = from.getAmount().multiply(PLN_TO_EUR);
        } else {
            newAmount = from.getAmount().divide(PLN_TO_EUR, 8, RoundingMode.HALF_UP);
        }
        return new CurrencyRatesServiceResponse(new Money(newAmount, to), null);
    }
}
