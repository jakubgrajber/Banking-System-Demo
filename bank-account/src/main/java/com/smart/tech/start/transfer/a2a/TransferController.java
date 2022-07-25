package com.smart.tech.start.transfer.a2a;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.domain.utilities.CheckingBankAccountMapper;
import com.smart.tech.start.domain.utilities.Money;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.request.TransferA2ARequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Currency;

import static com.smart.tech.start.jwt.JwtUtil.extractSubject;
import static com.smart.tech.start.jwt.JwtUtil.getAuthorizationHeader;
import static com.smart.tech.start.request.TransactionStatus.*;

@RestController
@RequestMapping("api/account")
@AllArgsConstructor
@Slf4j
public class TransferController {

    private AccountService accountService;
    private CheckingBankAccountMapper mapper;

    @PutMapping
    public ResponseEntity<TransferA2ARequest> processA2ATransfer(@RequestBody TransferA2ARequest bodyRequest, HttpServletRequest servletRequest) {

        String header = getAuthorizationHeader(servletRequest);
        String userEmail = extractSubject(servletRequest);

        if (!accountService.emailsMatch(bodyRequest.getSenderAccountNumber(), userEmail)){
            bodyRequest.setTransactionStatus(CANCELLED);
            return new ResponseEntity<>(bodyRequest, HttpStatus.FORBIDDEN);
        }

        CheckingBankAccountEntity senderAccountEntity = null;
        CheckingBankAccountEntity recipientAccountEntity = null;

        try {
            senderAccountEntity = accountService.findById(bodyRequest.getSenderAccountNumber());
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
            bodyRequest.setTransactionStatus(CANCELLED);
            bodyRequest.setStatusDescription(exception.getMessage());
            return new ResponseEntity<>(bodyRequest, HttpStatus.BAD_REQUEST);
        }

        try {
            recipientAccountEntity = accountService.findById(bodyRequest.getRecipientAccountNumber());
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
            bodyRequest.setTransactionStatus(CANCELLED);
            bodyRequest.setStatusDescription(exception.getMessage());
            return new ResponseEntity<>(bodyRequest, HttpStatus.BAD_REQUEST);
        }

        CheckingBankAccount senderAccount = mapper.entityToDomainModel(senderAccountEntity);
        CheckingBankAccount recipientAccount = mapper.entityToDomainModel(recipientAccountEntity);

//        Money moneyToTransfer = new Money(bodyRequest.getAmount(), Currency.getInstance(bodyRequest.getTransferCurrencyCode()));
        Money moneyToTransfer = new Money(bodyRequest.getAmount(), Currency.getInstance(bodyRequest.getTransferCurrencyCode()));


        try {
            senderAccount.send(moneyToTransfer, recipientAccount);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
            bodyRequest.setTransactionStatus(NOT_SUFFICIENT_FUNDS);
            bodyRequest.setStatusDescription(exception.getMessage());
            return new ResponseEntity<>(bodyRequest, HttpStatus.BAD_REQUEST);
        }

        senderAccountEntity.setBalance(senderAccount.getBalance().getAmount());
        recipientAccountEntity.setBalance(recipientAccount.getBalance().getAmount());

        accountService.updateBalance(senderAccountEntity);
        accountService.updateBalance(recipientAccountEntity);

        bodyRequest.setTransactionStatus(DONE);
        bodyRequest.setRecipientCurrencyCode(recipientAccount.getCurrency().getCurrencyCode());
//        bodyRequest.setRecipientCurrencyExchangeRate(recipientAccount.getTransactionRate());
        bodyRequest.setSenderCurrencyCode(senderAccount.getCurrency().getCurrencyCode());
//        bodyRequest.setSenderCurrencyExchangeRate(senderAccount.getTransactionRate());
        return new ResponseEntity<>(bodyRequest, HttpStatus.OK);
    }
}
