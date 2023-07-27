package com.spring.pass.job.passticket;

import com.spring.pass.domain.Booking;
import com.spring.pass.domain.constant.BookingStatus;
import com.spring.pass.repository.BookingRepository;
import com.spring.pass.repository.PassTicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Configuration
public class UsePassTicketsJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final PassTicketRepository passTicketRepository;
    private final BookingRepository bookingRepository;

    @Bean
    public Job usePassTicketsJob() {
        return jobBuilderFactory.get("usePassTicketsJob")
                .start(usePassTicketsStep())
                .build();
    }

    @Bean
    public Step usePassTicketsStep() {
        return stepBuilderFactory.get("usePassTicketsStep")
                .<Booking, Future<Booking>>chunk(CHUNK_SIZE)
                .reader(usePassTicketsItemReader())
                .processor(usePassTicketsAsyncItemProcessor())
                .writer(usePassTicketsAsyncItemWriter())
                .build();
    }



    @Bean
    public JpaCursorItemReader<Booking> usePassTicketsItemReader() {
        return new JpaCursorItemReaderBuilder<Booking>()
                .name("usePassesItemReader")
                .entityManagerFactory(entityManagerFactory)
                //예약 완료이고 종료 일시가 과거인데 아직 이용권 사용(usedPass)가 false인 Booking
                .queryString("select b from Booking b join fetch b.passTicket where b.status = :status and b.usedPass = false and b.endedAt < :endedAt order by b.id")
                .parameterValues(Map.of("status", BookingStatus.COMPLETED, "endedAt", LocalDateTime.now()))
                .build();
    }

    /**
     * ItemProcessor의 수행이 오래걸려 병목이 생기는 경우에 AsyncItemProcessor, AsyncItemWriter를 사용하면 성능을 향상시킬 수 있음
     */
    @Bean
    public AsyncItemProcessor<Booking, Booking> usePassTicketsAsyncItemProcessor() {
        AsyncItemProcessor<Booking, Booking> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(usePassTicketsItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<Booking, Booking> usePassTicketsItemProcessor() {
        return booking -> {
            booking.getPassTicket().completeBooking();
            booking.usePassTicket();
            return booking;
        };
    }

    private AsyncItemWriter<Booking> usePassTicketsAsyncItemWriter() {
        AsyncItemWriter<Booking> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate(usePassTicketsItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public ItemWriter<Booking> usePassTicketsItemWriter() {
        return bookings -> {
            for (Booking booking : bookings) {
                int updatedCount = passTicketRepository.updateRemainingCount(
                        booking.getPassTicket().getId(),
                        booking.getPassTicket().getRemainingCount()
                );
                if (updatedCount > 0) {
                    bookingRepository.updateUsedPass(
                            booking.getPassTicket().getId(),
                            booking.isUsedPass()
                    );
                }
            }
        };
    }
}
