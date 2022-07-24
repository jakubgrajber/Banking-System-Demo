package com.smart.tech.start.bank.account.management;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smart.tech.start.bank.account.management.client.AccountClient;
import com.smart.tech.start.request.BankAccountRegistrationRequest;
import com.smart.tech.start.request.BankAccountRemovalRequest;
import com.smart.tech.start.user.account.management.registration.entity.UserEntity;
import com.smart.tech.start.user.account.management.registration.service.BankAccountService;
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
    private BankAccountService bankAccountService;

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
    public ResponseEntity<String> sendRemoveRequest(@RequestBody BankAccountRemovalRequest bodyRequest, HttpServletRequest httpServletRequest) {

        long userId;

        try {
            userId = bankAccountService.findById(bodyRequest.getAccountNumber()).getUser().getId();
        } catch (RuntimeException exception){
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String header = httpServletRequest.getHeader(AUTHORIZATION);
        String token = header.substring("Bearer ".length());
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        String username = decodedJWT.getSubject();

        if (!userService.getUserById(userId).getEmail().equals(username)){
            return new ResponseEntity<>("The user is not authorized to this account", HttpStatus.FORBIDDEN);
        } else {
            bodyRequest.setUserEmail(username);
            ResponseEntity<String> response = accountClient.removeAccount(header, bodyRequest);
            if (response.getStatusCode().equals(HttpStatus.OK)){
                bankAccountService.delete(bodyRequest.getAccountNumber());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
}
