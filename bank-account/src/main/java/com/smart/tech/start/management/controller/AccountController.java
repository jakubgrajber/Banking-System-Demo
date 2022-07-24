package com.smart.tech.start.management.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smart.tech.start.management.client.UserClient;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import com.smart.tech.start.request.BankAccountRemovalRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.math.BigInteger;

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

        if (!bodyRequest.getUserEmail().equals(username)) {
            return new ResponseEntity<>("The requested email and does not match the issuer's email.", HttpStatus.FORBIDDEN);
        } else {
            accountService.register(bodyRequest);
            CheckingBankAccountEntity account = accountService.findByEmail(bodyRequest.getUserEmail());
            userClient.updateUserWithNewAccountNumber(header, account.getAccountNumber(), bodyRequest.getUserEmail());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(@RequestBody BankAccountRemovalRequest bodyRequest, HttpServletRequest servletRequest) {

        String header = servletRequest.getHeader(AUTHORIZATION);
        String token = header.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        if (!bodyRequest.getUserEmail().equals(username)) {
            return new ResponseEntity<>("The requested email and does not match the issuer's email.", HttpStatus.FORBIDDEN);
        } else {
            if (accountService.findById(bodyRequest.getAccountNumber().toString()).getBalance().equals(new BigDecimal(BigInteger.ZERO))){
                return new ResponseEntity<>("The account with balance greater than zero cannot be removed.", HttpStatus.FORBIDDEN);
            }
            accountService.delete(bodyRequest.getAccountNumber());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
