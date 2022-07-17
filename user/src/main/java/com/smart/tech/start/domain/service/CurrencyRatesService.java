package com.smart.tech.start.domain.service;

import com.smart.tech.start.domain.utilites.Money;

import java.util.Currency;

public interface CurrencyRatesService {
    Money exchange(Money from, Currency to);
}
