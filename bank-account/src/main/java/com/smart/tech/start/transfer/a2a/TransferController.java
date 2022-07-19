package com.smart.tech.start.transfer.a2a;

import com.smart.tech.start.domain.account.CheckingBankAccount;
import com.smart.tech.start.domain.utilities.CheckingBankAccountMapper;
import com.smart.tech.start.management.entity.AccountEntity;
import com.smart.tech.start.management.service.AccountService;
import com.smart.tech.start.model.TransferA2ARequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/transfer")
@AllArgsConstructor
public class TransferController {

    private AccountService accountService;
    private CheckingBankAccountMapper mapper;

    @PatchMapping
    public void processA2ATransfer(@RequestBody TransferA2ARequest request){

        AccountEntity senderAccountEntity = null;
        AccountEntity recipientAccountEntity = null;

        Optional<AccountEntity> optionalSenderAccount = accountService.findById(request.getSenderAccountNumber());
        if (optionalSenderAccount.isPresent()){
            senderAccountEntity = optionalSenderAccount.get();
        }

        Optional<AccountEntity> optionalRecipientAccount = accountService.findById(request.getRecipientAccountNumber());
        if (optionalRecipientAccount.isPresent()){
            recipientAccountEntity = optionalRecipientAccount.get();
        }

        CheckingBankAccount senderAccount = mapper.map(senderAccountEntity);
        CheckingBankAccount recipientAccount = mapper.map(recipientAccountEntity);

        System.out.println(senderAccount);
        System.out.println(recipientAccount);

    }
}
