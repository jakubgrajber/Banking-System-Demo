package com.smart.tech.start.management.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient("user")
public interface UserClient {

    @RequestMapping(method = RequestMethod.POST, value = "api/bank-account?accountNumber={id}&userEmail={email}")
    void updateUserWithNewAccountNumber(@PathVariable("id") UUID accountNumber, @PathVariable("email") String userEmail);
}
