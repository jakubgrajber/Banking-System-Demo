package com.smart.tech.start.controller;

import com.smart.tech.start.client.AccountClient;
import com.smart.tech.start.model.TransferA2AEntity;
import com.smart.tech.start.model.TransferA2AService;
import com.smart.tech.start.request.TransferA2ARequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.smart.tech.start.jwt.JwtUtil.getAuthorizationHeader;

@RestController
@RequestMapping("api/transfer")
@AllArgsConstructor
public class TransferController {

    private TransferA2AService transferA2AService;
    private AccountClient accountClient;

    @PostMapping
    public ResponseEntity<String> createNewTransferRequest(@RequestBody TransferA2ARequest request, HttpServletRequest servletRequest) {

        String header = getAuthorizationHeader(servletRequest);

        TransferA2AEntity transfer = new TransferA2AEntity(
                UUID.fromString(request.getSenderAccountNumber()),
                request.getAmount(),
                request.getTransferCurrencyCode(),
                UUID.fromString(request.getRecipientAccountNumber()),
                request.getTitle(),
                LocalDateTime.now()
        );

        ResponseEntity<TransferA2ARequest> transferResponse = accountClient.sendTransferRequest(header, request);

        TransferA2ARequest responseBody = transferResponse.getBody();
        transfer.setStatus(responseBody.getTransactionStatus().toString());
        transfer.setStatusDescription(responseBody.getStatusDescription());
        transfer.setRecipientCurrencyCode(responseBody.getRecipientCurrencyCode());
        transfer.setRecipientCurrencyExchangeRate(responseBody.getRecipientCurrencyExchangeRate());
        transfer.setSenderCurrencyCode(responseBody.getSenderCurrencyCode());
        transfer.setSenderCurrencyExchangeRate(responseBody.getSenderCurrencyExchangeRate());
        transferA2AService.save(transfer);
        return new ResponseEntity<>(responseBody.getStatusDescription(), transferResponse.getStatusCode());
    }
}
