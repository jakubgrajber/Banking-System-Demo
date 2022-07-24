package com.smart.tech.start.bank.account.client;

import com.smart.tech.start.request.BankAccountRegistrationRequest;
import com.smart.tech.start.request.BankAccountRemovalRequest;
import com.smart.tech.start.user.account.management.entity.UserEntity;
import com.smart.tech.start.user.account.management.service.BankAccountService;
import com.smart.tech.start.user.account.management.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.smart.tech.start.jwt.JwtUtil.extractSubject;
import static com.smart.tech.start.jwt.JwtUtil.getAuthorizationHeader;

@RestController
@RequestMapping("api/user/bank-account")
@AllArgsConstructor
@Slf4j
public class BankAccountRequestsController {

    private AccountClient accountClient;
    private UserService userService;
    private BankAccountService bankAccountService;

    @PostMapping
    public ResponseEntity<String> sendRegisterRequest(@RequestBody BankAccountRegistrationRequest bodyRequest, HttpServletRequest servletRequest) {

        UserEntity userEntity;

        String header = getAuthorizationHeader(servletRequest);
        String userEmail = extractSubject(servletRequest);

        try {
            userEntity = userService.getUserByEmail(userEmail);
        } catch (RuntimeException exception) {
            log.error("New account request from user with invalid email: " + userEmail);
            return new ResponseEntity<>("No user with email: " + userEmail, HttpStatus.BAD_REQUEST);
        }

        if (userEntity.hasPermissionToPerformAction()) {
            bodyRequest.setUserEmail(userEmail);
            accountClient.createAccount(header, bodyRequest);
        } else {
            return new ResponseEntity<>("This user account does not have permission to perform this action.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> sendRemoveRequest(@RequestBody BankAccountRemovalRequest bodyRequest, HttpServletRequest servletRequest) {

        long userId;

        try {
            userId = bankAccountService.getAccountOwnerId(bodyRequest.getAccountNumber());
        } catch (RuntimeException exception) {
            log.error(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String header = getAuthorizationHeader(servletRequest);
        String userEmail = extractSubject(servletRequest);

        if (!userService.getUserEmail(userId).equals(userEmail)) {
            return new ResponseEntity<>("The user is not authorized to this account", HttpStatus.FORBIDDEN);
        } else {
            bodyRequest.setUserEmail(userEmail);
            ResponseEntity<String> response = accountClient.removeAccount(header, bodyRequest);
            if (response.getStatusCode().equals(HttpStatus.OK)) {
                bankAccountService.delete(bodyRequest.getAccountNumber());
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
    }
}
