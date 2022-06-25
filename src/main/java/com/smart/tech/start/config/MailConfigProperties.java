package com.smart.tech.start.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailConfigProperties {

    private String subject;
    private String senderEmail;
    private String encoding;
}
