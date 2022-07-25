package com.smart.tech.start.domain.service;

import com.smart.tech.start.domain.utilities.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class CurrencyRatesServiceResponse {
    private final Money money;
    private final BigDecimal rate;
}
