package com.smart.tech.start.client;

import com.smart.tech.start.request.TransferA2ARequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "bank-account", path = "api/account")
public interface AccountClient {

    @RequestMapping(method = RequestMethod.PUT)
    ResponseEntity<TransferA2ARequest> sendTransferRequest(@RequestHeader(value = "Authorization") String header, @RequestBody TransferA2ARequest request);

}
