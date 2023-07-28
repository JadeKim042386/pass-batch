package com.spring.pass.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Import(BatchConfig.class)
@EnableAutoConfiguration
@EntityScan("com.spring.pass.domain")
@EnableJpaRepositories("com.spring.pass.repository")
@EnableTransactionManagement
public class TestBatchConfig {
}
