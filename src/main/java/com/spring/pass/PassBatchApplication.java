package com.spring.pass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class PassBatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(PassBatchApplication.class, args);
    }
}
