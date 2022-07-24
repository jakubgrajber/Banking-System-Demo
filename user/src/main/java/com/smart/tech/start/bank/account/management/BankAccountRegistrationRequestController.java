package com.smart.tech.start.bank.account.management;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smart.tech.start.bank.account.management.client.AccountClient;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import com.smart.tech.start.user.account.management.registration.entity.UserEntity;
import com.smart.tech.start.user.account.management.registration.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("api/user/bank-account")
@AllArgsConstructor
@Slf4j
public class BankAccountRegistrationRequestController {

    private AccountClient accountClient;
    private UserService userService;

    @GetMapping
    public String justATest(){
        return "it works".toUpperCase();
    }

    @PostMapping
    public ResponseEntity<String> sendRegisterRequest(@RequestBody BankAccountRegistrationRequest bodyRequest, HttpServletRequest httpServletRequest){

        UserEntity userEntity;

        String header = httpServletRequest.getHeader(AUTHORIZATION);
        String token = header.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        try {
            userEntity = userService.getUserByEmail(username);
        } catch (RuntimeException exception){
            log.error("New account request from user with invalid email: " + username);
            return new ResponseEntity<>("No user with email: " + username, HttpStatus.BAD_REQUEST);
        }

        if (userEntity.isAccountNonExpired() && userEntity.isAccountNonLocked() && userEntity.isEnabled() && userEntity.isCredentialsNonExpired()){
            bodyRequest.setUserEmail(username);
            accountClient.createAccount(header, bodyRequest);
        } else{
            return new ResponseEntity<>("This user account does not have permission to perform this action.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public void sendRemoveRequest(@RequestParam UUID bankAccountNumber) {

        accountClient.removeAccount(bankAccountNumber);
    }


}
