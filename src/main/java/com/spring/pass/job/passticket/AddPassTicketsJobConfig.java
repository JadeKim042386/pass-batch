package com.spring.pass.job.passticket;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class AddPassTicketsJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final AddPassTicketsTasklet addPassTicketsTasklet;

    @Bean
    public Job addPassTicketsJob(){
        return jobBuilderFactory.get("addPassTicketsJob")
                .start(addPassTicketsStep())
                .build();
    }

    @Bean
    public Step addPassTicketsStep() {
        return stepBuilderFactory.get("addPassTicketsStep")
                .tasklet(addPassTicketsTasklet)
                .build();
    }
}
