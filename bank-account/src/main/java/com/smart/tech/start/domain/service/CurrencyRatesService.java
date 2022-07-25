package com.smart.tech.start.domain.service;


import com.smart.tech.start.domain.utilities.Money;

import java.util.Currency;

public interface CurrencyRatesService {
    Money exchange(Money from, Currency to);
//    CurrencyRatesServiceResponse exchange(Money from, Currency to);
}
