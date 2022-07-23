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

import java.util.UUID;

@RestController
@RequestMapping("api/user/bank-account")
@AllArgsConstructor
@Slf4j
public class BankAccountRegistrationRequestController {

    private AccountClient accountClient;
    private UserService userService;

    @PostMapping
    public ResponseEntity<String> sendRequest(@RequestBody BankAccountRegistrationRequest request){

        UserEntity userEntity;

        try {
            userEntity = userService.getUserByEmail(request.getUserEmail());
        } catch (RuntimeException exception){
            log.error("New account request from user with invalid email: " + request.getUserEmail());
            return new ResponseEntity<>("No user with email: " + request.getUserEmail(), HttpStatus.BAD_REQUEST);
        }

        if (userEntity.isAccountNonExpired() && userEntity.isAccountNonLocked() && userEntity.isEnabled() && userEntity.isCredentialsNonExpired()){
            accountClient.createAccount(request);
        } else{
            return new ResponseEntity<>("This user account does not have permission to perform this action.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public void deleteAccount(@RequestParam UUID bankAccountNumber) {
        accountClient.removeAccount(bankAccountNumber);
    }


}
