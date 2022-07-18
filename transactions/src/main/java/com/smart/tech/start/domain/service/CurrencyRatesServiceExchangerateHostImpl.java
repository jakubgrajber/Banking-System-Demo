package com.smart.tech.start.domain.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.tech.start.domain.utilities.Money;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

/**
 * This CurrencyRatesService implementation is just for training purposes
 * https://exchangerate.host/#/
 */

public class CurrencyRatesServiceExchangerateHostImpl implements CurrencyRatesService {

    @Override
    public Money exchange(Money from, Currency to) {
        BigDecimal result = BigDecimal.ZERO;
        String url = urlBuilder(from.getCurrency(), to, from.getAmount());
        try {
            result = callExchangerateAPI(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Money(result, to);
    }

    private BigDecimal callExchangerateAPI(String query) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(query, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        JsonNode name = root.path("result");

        return new BigDecimal(name.asText());
    }

    private String urlBuilder(Currency from, Currency to, BigDecimal amount) {
        return "https://api.exchangerate.host/convert?from=" + from.getCurrencyCode()
                + "&to=" + to.getCurrencyCode() + "&amount=" + amount.toString();
    }
}
