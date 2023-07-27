package com.spring.pass.job.statistics;

import com.spring.pass.domain.Booking;
import com.spring.pass.domain.Statistics;
import com.spring.pass.repository.StatisticsRepository;
import com.spring.pass.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MakeStatisticsJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final StatisticsRepository statisticsRepository;
    private final MakeDailyStatisticsTasklet makeDailyStatisticsTasklet;
    private final MakeWeeklyStatisticsTasklet makeWeeklyStatisticsTasklet;

    @Bean
    public Job makeStatisticJob() {
        Flow addStatisticsFlow = new FlowBuilder<Flow>("addStatisticsFlow")
                .start(addStatisticsStep())
                .build();

        Flow makeDailyStatisticsFlow = new FlowBuilder<Flow>("makeDailyStatisticsFlow")
                .start(makeDailyStatisticsStep())
                .build();
        Flow makeWeeklyStatisticsFlow = new FlowBuilder<Flow>("makeWeeklyStatisticsFlow")
                .start(makeWeeklyStatisticsStep())
                .build();
        Flow parallelMakeStatisticsFlow = new FlowBuilder<Flow>("parallelMakeStatisticsFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(makeDailyStatisticsFlow, makeWeeklyStatisticsFlow)
                .build();

        return jobBuilderFactory.get("makeStatisticJob")
                .start(addStatisticsFlow)
                .next(parallelMakeStatisticsFlow)
                .build()
                .build();
    }

    @Bean
    public Step addStatisticsStep() {
        return stepBuilderFactory.get("addStatisticsStep")
                .<Booking, Booking>chunk(CHUNK_SIZE)
                .reader(addStatisticsItemReader(null, null))
                .writer(addStatisticsItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaCursorItemReader<Booking> addStatisticsItemReader(
            @Value("#{jobParameters[from]}") String fromString,
            @Value("#{jobParameters[to]}") String toString
    ) {
        LocalDateTime from = LocalDateTimeUtils.parse(fromString);
        LocalDateTime to = LocalDateTimeUtils.parse(toString);

        return new JpaCursorItemReaderBuilder<Booking>()
                .name("addStatisticsItemReader")
                .entityManagerFactory(entityManagerFactory)
                //JobParameter를 받아 종료 일시(endedAt) 기준으로 통계 대상 예약(Booking)을 조회
                .queryString("select b from Booking b where b.endedAt between :from and :to")
                .parameterValues(Map.of("from", from, "to", to))
                .build();
    }

    @Bean
    public ItemWriter<Booking> addStatisticsItemWriter() {
        return bookings -> {
            LinkedHashMap<LocalDateTime, Statistics> statisticMap = new LinkedHashMap<>();

            for (Booking booking : bookings) {
                LocalDateTime statisticsAt = booking.getStatisticsAt();
                Statistics statistics = statisticMap.get(statisticsAt);

                if (statistics == null) {
                    statisticMap.put(statisticsAt, Statistics.fromBooking(booking));
                } else {
                    statistics.add(booking);
                }
            }
            List<Statistics> statistics = new ArrayList<>(statisticMap.values());
            statisticsRepository.saveAll(statistics);
        };
    }

    @Bean
    public Step makeDailyStatisticsStep() {
        return stepBuilderFactory.get("makeDailyStatisticsStep")
                .tasklet(makeDailyStatisticsTasklet)
                .build();
    }

    @Bean
    public Step makeWeeklyStatisticsStep() {
        return stepBuilderFactory.get("makeWeeklyStatisticsStep")
                .tasklet(makeWeeklyStatisticsTasklet)
                .build();
    }
}
