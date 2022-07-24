package com.smart.tech.start.transfer.a2a;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.domain.utilities.CheckingBankAccountMapper;
import com.smart.tech.start.domain.utilities.Money;
import com.smart.tech.start.management.entity.CheckingBankAccountEntity;
import com.smart.tech.start.management.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Currency;
import java.util.Optional;

@RestController
@RequestMapping("api/account")
@AllArgsConstructor
@Slf4j
public class TransferController {

    private AccountService accountService;
    private CheckingBankAccountMapper mapper;

    @PatchMapping
    public ResponseEntity<String> processA2ATransfer(@RequestBody TransferA2ARequest request) {

        CheckingBankAccountEntity senderAccountEntity = null;
        CheckingBankAccountEntity recipientAccountEntity = null;

        try {
            senderAccountEntity = accountService.findById(request.getSenderAccountNumber());
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
            return new ResponseEntity<>("Invalid sender bank account number.", HttpStatus.BAD_REQUEST);
        }

        try {
            recipientAccountEntity = accountService.findById(request.getRecipientAccountNumber());
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
            return new ResponseEntity<>("Invalid recipient bank account number.", HttpStatus.BAD_REQUEST);

        }

        CheckingBankAccount senderAccount = mapper.entityToDomainModel(senderAccountEntity);
        CheckingBankAccount recipientAccount = mapper.entityToDomainModel(recipientAccountEntity);

        Money moneyToTransfer = new Money(request.getAmount(), Currency.getInstance(request.getCurrencyCode()));

        try {
            senderAccount.send(moneyToTransfer, recipientAccount);
        } catch (RuntimeException exception) {
            log.error(exception.getMessage(), exception);
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

        senderAccountEntity.setBalance(senderAccount.getBalance().getAmount());
        recipientAccountEntity.setBalance(recipientAccount.getBalance().getAmount());

        accountService.updateBalance(senderAccountEntity);
        accountService.updateBalance(recipientAccountEntity);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
