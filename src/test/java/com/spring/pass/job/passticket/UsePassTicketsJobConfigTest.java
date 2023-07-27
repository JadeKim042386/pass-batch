package com.spring.pass.job.passticket;

import com.spring.pass.config.TestBatchConfig;
import com.spring.pass.config.TestJpaConfig;
import com.spring.pass.domain.Booking;
import com.spring.pass.domain.PassTicket;
import com.spring.pass.repository.BookingRepository;
import com.spring.pass.repository.PassTicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        UsePassTicketsJobConfig.class,
        TestBatchConfig.class,
        TestJpaConfig.class
})
class UsePassTicketsJobConfigTest {
    private JobLauncherTestUtils jobLauncherTestUtils;
    private PassTicketRepository passTicketRepository;
    private BookingRepository bookingRepository;

    @Autowired
    public UsePassTicketsJobConfigTest(JobLauncherTestUtils jobLauncherTestUtils, PassTicketRepository passTicketRepository, BookingRepository bookingRepository) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.passTicketRepository = passTicketRepository;
        this.bookingRepository = bookingRepository;
    }

    @DisplayName("이용권 차감 테스트")
    @Test
    void usePassTicket() {
        // Given

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("usePassTicketsStep");
        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<PassTicket> passTickets = passTicketRepository.findAll();
        PassTicket passTicket = passTickets.get(0);
        assertThat(passTicket.getRemainingCount()).isEqualTo(99);

        List<Booking> bookings = bookingRepository.findAll();
        Booking booking = bookings.get(0);
        assertThat(booking.isUsedPass()).isEqualTo(true);
    }
}
