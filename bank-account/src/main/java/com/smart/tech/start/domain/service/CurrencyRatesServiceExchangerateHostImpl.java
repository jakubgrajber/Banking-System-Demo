package com.smart.tech.start.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.tech.start.domain.utilities.Money;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

/**
 * This CurrencyRatesService implementation is just for training purposes
 * https://exchangerate.host/#/
 */

public class CurrencyRatesServiceExchangerateHostImpl implements CurrencyRatesService {

    private static final String RESULT = "result";
    private static final String RATE = "rate";


    @Override
    public CurrencyRatesServiceResponse exchange(Money from, Currency to) {
        Map<String, BigDecimal> results = null;
        String url = urlBuilder(from.getCurrency(), to, from.getAmount());
        try {
            results = callExchangerateAPI(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new CurrencyRatesServiceResponse(new Money(results.get(RESULT),to), results.get(RATE));
    }

    private Map<String, BigDecimal> callExchangerateAPI(String query) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(query, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode result = root.path(RESULT);
        JsonNode rate = root.path("info").path(RATE);

        Map<String, BigDecimal> results = new HashMap<>();
        results.put(RESULT, result.decimalValue());
        results.put(RATE, rate.decimalValue());

        return results;
    }

    private String urlBuilder(Currency from, Currency to, BigDecimal amount) {
        return "https://api.exchangerate.host/convert?from=" + from.getCurrencyCode()
                + "&to=" + to.getCurrencyCode() + "&amount=" + amount.toString();
    }
}
