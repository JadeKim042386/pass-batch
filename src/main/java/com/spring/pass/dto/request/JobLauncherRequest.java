package com.spring.pass.dto.request;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;

import java.util.Properties;

public record JobLauncherRequest(
        String name,
        Properties jobParameters
) {
    public JobParameters getJobParameters() {
        return new JobParametersBuilder(jobParameters).toJobParameters();
    }
}
