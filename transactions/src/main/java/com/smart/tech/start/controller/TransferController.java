package com.smart.tech.start.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/transfer")
public class TransferController {

    @PostMapping
    public void createNewTransferRequest(){
    }
}
