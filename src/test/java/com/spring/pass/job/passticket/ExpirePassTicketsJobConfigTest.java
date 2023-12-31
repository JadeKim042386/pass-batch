package com.spring.pass.job.passticket;

import com.spring.pass.config.TestBatchConfig;
import com.spring.pass.config.TestJpaConfig;
import com.spring.pass.domain.PassTicket;
import com.spring.pass.domain.constant.PassTicketStatus;
import com.spring.pass.repository.PassTicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        ExpirePassTicketsJobConfig.class,
        TestBatchConfig.class,
        TestJpaConfig.class
})
class ExpirePassTicketsJobConfigTest {
    private JobLauncherTestUtils jobLauncherTestUtils;
    private PassTicketRepository passTicketRepository;

    @Autowired
    public ExpirePassTicketsJobConfigTest(JobLauncherTestUtils jobLauncherTestUtils, PassTicketRepository passTicketRepository) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.passTicketRepository = passTicketRepository;
    }

    @DisplayName("이용권 만료 기능 테스트")
    @Test
    public void expirePassTicketsStep() throws Exception {
        // Given
        addPassEntities(10);
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("expirePassTicketsStep");
        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<PassTicket> passTickets = passTicketRepository.findAll();
        for (PassTicket passTicket : passTickets) {
            if (passTicket.getEndedAt().isBefore(LocalDateTime.now())) {
                assertThat(passTicket.getStatus()).isEqualTo(PassTicketStatus.EXPIRED);
                assertThat(passTicket.getExpiredAt()).isNotNull();
            }
        }
    }

    private void addPassEntities(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<PassTicket> passEntities = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            PassTicket passTicket = PassTicket.of(
                    1L,
                    "C" + 1000000 + i,
                    PassTicketStatus.PROGRESSED,
                    random.nextInt(11),
                    now.minusDays(60),
                    now.minusDays(1),
                    null
            );
            passEntities.add(passTicket);

        }
        passTicketRepository.saveAll(passEntities);
    }
}
