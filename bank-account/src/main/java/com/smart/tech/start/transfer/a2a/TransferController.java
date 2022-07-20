package com.smart.tech.start.transfer.a2a;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.domain.utilities.CheckingBankAccountMapper;
import com.smart.tech.start.domain.utilities.Money;
import com.smart.tech.start.management.entity.AccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.model.TransferA2ARequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

@RestController
@RequestMapping("api/transfer")
@AllArgsConstructor
@Slf4j
public class TransferController {

    private AccountService accountService;
    private CheckingBankAccountMapper mapper;

    @PatchMapping
    public ResponseEntity<String> processA2ATransfer(@RequestBody TransferA2ARequest request) {

        AccountEntity senderAccountEntity = null;
        AccountEntity recipientAccountEntity = null;

        Optional<AccountEntity> optionalSenderAccount = accountService.findById(request.getSenderAccountNumber());
        if (optionalSenderAccount.isPresent()) {
            senderAccountEntity = optionalSenderAccount.get();
        } else {
            return new ResponseEntity<>("Invalid sender bank account number.", HttpStatus.BAD_REQUEST);
        }

        Optional<AccountEntity> optionalRecipientAccount = accountService.findById(request.getRecipientAccountNumber());
        if (optionalRecipientAccount.isPresent()) {
            recipientAccountEntity = optionalRecipientAccount.get();
        } else {
            return new ResponseEntity<>("Invalid recipient bank account number.", HttpStatus.BAD_REQUEST);
        }

        CheckingBankAccount senderAccount = mapper.entityToDomainModel(senderAccountEntity);
        CheckingBankAccount recipientAccount = mapper.entityToDomainModel(recipientAccountEntity);

//        senderAccount.setBalance(new Money(new BigDecimal(5000), Currency.getInstance(request.getCurrencyCode())));

        Money moneyToTransfer = new Money(request.getAmount(), Currency.getInstance(request.getCurrencyCode()));

        try {
            senderAccount.sendTransfer(moneyToTransfer, recipientAccount);
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
