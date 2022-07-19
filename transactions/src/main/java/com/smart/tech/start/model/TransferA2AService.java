package com.smart.tech.start.model;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TransferA2AService {

    private TransferA2ARepository transferA2ARepository;

    public void save(TransferA2AEntity transfer) {
        transferA2ARepository.save(transfer);
    }
}
