package com.smart.tech.start.bank.account.management;

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

        try {
            userEntity = userService.getUserByEmail(bodyRequest.getUserEmail());
        } catch (RuntimeException exception){
            log.error("New account request from user with invalid email: " + bodyRequest.getUserEmail());
            return new ResponseEntity<>("No user with email: " + bodyRequest.getUserEmail(), HttpStatus.BAD_REQUEST);
        }

        if (userEntity.isAccountNonExpired() && userEntity.isAccountNonLocked() && userEntity.isEnabled() && userEntity.isCredentialsNonExpired()){
            String header = httpServletRequest.getHeader(AUTHORIZATION);
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
