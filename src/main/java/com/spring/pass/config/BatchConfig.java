package com.spring.pass.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Configuration;

/**
 * <h3>@EnableBatchProcessing</h3>
 * <ul>
 *     <li>Spring Batch 기능을 활성화하고 배치 작업을 설정하기 위한 기본 구성을 제공</li>
 *     <li>JobRepository, JobLauncher, JobRegistry, PlatformTransactionManager, JobBuilderFactory, StepBuilderFactory 을 빈으로 제공</li>
 * </ul>
 */
@EnableBatchProcessing
@Configuration
public class BatchConfig {
}
