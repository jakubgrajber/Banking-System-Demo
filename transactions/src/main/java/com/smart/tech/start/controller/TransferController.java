package com.smart.tech.start.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.tech.start.model.TransferA2AEntity;
import com.smart.tech.start.model.TransferA2AService;
import com.smart.tech.start.request.TransferA2ARequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("api/transfer")
@AllArgsConstructor
public class TransferController {

    private TransferA2AService transferA2AService;

    @PostMapping
    public void createNewTransferRequest(@RequestBody TransferA2ARequest request) throws URISyntaxException, IOException, InterruptedException {

        TransferA2AEntity transfer = new TransferA2AEntity(
                UUID.fromString(request.getSenderAccountNumber()),
                request.getAmount(),
                request.getCurrencyCode(),
                UUID.fromString(request.getRecipientAccountNumber()),
                request.getTitle(),
                LocalDateTime.now()
        );

        transferA2AService.save(transfer);

        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);
        HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(jsonRequest);

        HttpRequest transferRequest = HttpRequest.newBuilder()
                .uri(new URI("http://bank-account:7070/api/account"))
                .method("PATCH", requestBody).build();

        HttpClient httpClient = HttpClient.newHttpClient();
        httpClient.send(transferRequest, HttpResponse.BodyHandlers.ofString());
    }

    @PatchMapping
    public void updateTransfer(){

    }
}
