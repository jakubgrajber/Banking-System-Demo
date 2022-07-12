package com.smart.tech.start.registration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties(prefix = "mail")
@Getter
@Setter
public class MailConfigProperties {

    @DurationUnit(ChronoUnit.MINUTES)
    private Duration confirmationTime;
}
