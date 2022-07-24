package com.smart.tech.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class TransactionsApplication {
    public static void main(String[] args) {
        SpringApplication.run(TransactionsApplication.class, args);
    }
}
