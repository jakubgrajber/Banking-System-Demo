package com.smart.tech.start.management.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smart.tech.start.management.client.UserClient;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService accountService;
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<String> registerNewAccount(@RequestBody BankAccountRegistrationRequest bodyRequest, HttpServletRequest servletRequest) {
        String header = servletRequest.getHeader(AUTHORIZATION);
        String token = header.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        if(!bodyRequest.getUserEmail().equals(username)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            accountService.register(bodyRequest);
            CheckingBankAccountEntity account = accountService.findByEmail(bodyRequest.getUserEmail());
            userClient.updateUserWithNewAccountNumber(header, account.getAccountNumber(), bodyRequest.getUserEmail());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteAccount(@RequestParam UUID accountNumber) {
        accountService.delete(accountNumber);
    }
}
