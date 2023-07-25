package com.spring.pass.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@TestConfiguration
@EnableAutoConfiguration
@EnableBatchProcessing
@EntityScan("com.spring.pass.domain")
@EnableJpaRepositories("com.spring.pass.repository")
@EnableTransactionManagement
public class TestBatchConfig {
}
