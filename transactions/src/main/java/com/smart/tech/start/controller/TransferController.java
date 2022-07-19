package com.smart.tech.start.controller;

import com.smart.tech.start.model.TransferA2AEntity;
import com.smart.tech.start.model.TransferA2ARequest;
import com.smart.tech.start.model.TransferA2AService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/transfer")
@AllArgsConstructor
public class TransferController {

    private TransferA2AService transferA2AService;

    @PostMapping
    public void createNewTransferRequest(@RequestBody TransferA2ARequest request){
        TransferA2AEntity transfer = new TransferA2AEntity(
                UUID.fromString(request.getSenderAccountNumber()),
                request.getAmount(),
                request.getCurrencyCode(),
                UUID.fromString(request.getRecipientAccountNumber()),
                request.getTitle(),
                LocalDateTime.now()
        );

        transferA2AService.save(transfer);

        // call to bank-account
    }

    @PatchMapping
    public void getTransferResult(){

    }
}
