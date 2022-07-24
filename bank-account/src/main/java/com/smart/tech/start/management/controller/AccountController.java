package com.smart.tech.start.management.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smart.tech.start.jwt.JwtUtil;
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

import static com.smart.tech.start.jwt.JwtUtil.extractSubject;
import static com.smart.tech.start.jwt.JwtUtil.getAuthorizationHeader;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/account")
public class AccountController {

    private final AccountService accountService;
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<String> registerNewAccount(@RequestBody BankAccountRegistrationRequest bodyRequest, HttpServletRequest servletRequest) {

        String header = getAuthorizationHeader(servletRequest);
        String userEmail = extractSubject(servletRequest);

        if (!bodyRequest.getUserEmail().equals(userEmail)) {
            return new ResponseEntity<>("The requested email and does not match the issuer's email.", HttpStatus.FORBIDDEN);
        } else {
            accountService.register(bodyRequest);
            CheckingBankAccountEntity account = accountService.findByEmail(bodyRequest.getUserEmail());
            userClient.updateUserWithNewAccountNumber(header, account.getAccountNumber(), userEmail);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(@RequestBody BankAccountRemovalRequest bodyRequest, HttpServletRequest servletRequest) {

        String header = getAuthorizationHeader(servletRequest);
        String userEmail = extractSubject(servletRequest);

        if (!bodyRequest.getUserEmail().equals(userEmail)) {
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
