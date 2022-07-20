package com.smart.tech.start.bank.account.management;

import lombok.AllArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("api/user/bank-account")
@AllArgsConstructor
public class BankAccountController {

    private WebClient.Builder webClientBuilder;

    @PostMapping
    public void createNewAccount(@RequestBody BankAccountRegistrationRequest request){
        MultiValueMap<String,String> bodyValues = new LinkedMultiValueMap<>();

        bodyValues.add("userEmail", request.getUserEmail());
        bodyValues.add("currencyCode", request.getCurrencyCode());

        webClientBuilder.build().post().uri("http://bank-account/api/account").body(BodyInserters.fromFormData(bodyValues));
    }
}
