package com.spring.pass.job.passticket;

import com.spring.pass.domain.PassTicket;
import com.spring.pass.domain.constant.PassTicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class ExpirePassTicketsJobConfig {
    private final int CHUNK_SIZE = 1;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job expirePassTicketsJob() {
        return jobBuilderFactory.get("expirePassTicketsJob")
                .start(expirePassTicketsStep())
                .build();
    }

    @Bean
    public Step expirePassTicketsStep() {
        return stepBuilderFactory.get("expirePassTicketsStep")
                .<PassTicket, PassTicket>chunk(CHUNK_SIZE)
                .reader(expirePassTicketsItemReader())
                .processor(expirePassTicketsItemProcessor())
                .writer(expirePassTicketsItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<PassTicket> expirePassTicketsItemReader() {
        return new JpaCursorItemReaderBuilder<PassTicket>()
                .name("expirePassTicketsItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select p from PassTicket p where p.status = :status and p.endedAt <= :endedAt order by p.id")
                .parameterValues(Map.of("status", PassTicketStatus.PROGRESSED, "endedAt", LocalDateTime.now()))
                .build();
    }

    @Bean
    public ItemProcessor<PassTicket, PassTicket> expirePassTicketsItemProcessor() {
        return passTicket -> {
            passTicket.changeStatus(PassTicketStatus.EXPIRED);
            passTicket.expire();
            return passTicket;
        };
    }

    @Bean
    public JpaItemWriter<PassTicket> expirePassTicketsItemWriter() {
        return new JpaItemWriterBuilder<PassTicket>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
