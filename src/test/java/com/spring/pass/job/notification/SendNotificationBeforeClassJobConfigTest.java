package com.spring.pass.job.notification;

import com.spring.pass.config.TestBatchConfig;
import com.spring.pass.config.TestJpaConfig;
import com.spring.pass.domain.Booking;
import com.spring.pass.domain.PassTicket;
import com.spring.pass.domain.UserAccount;
import com.spring.pass.domain.UserGroup;
import com.spring.pass.domain.constant.BookingStatus;
import com.spring.pass.domain.constant.PassTicketStatus;
import com.spring.pass.domain.constant.UserAccountStatus;
import com.spring.pass.repository.BookingRepository;
import com.spring.pass.repository.PassTicketRepository;
import com.spring.pass.repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        SendNotificationBeforeClassJobConfig.class,
        TestBatchConfig.class,
        TestJpaConfig.class
})
class SendNotificationBeforeClassJobConfigTest {
    private JobLauncherTestUtils jobLauncherTestUtils;
    private BookingRepository bookingRepository;
    private PassTicketRepository passTicketRepository;
    private UserAccountRepository userAccountRepository;

    @MockBean SendNotificationItemWriter sendNotificationItemWriter;

    @Autowired
    public SendNotificationBeforeClassJobConfigTest(JobLauncherTestUtils jobLauncherTestUtils, BookingRepository bookingRepository, PassTicketRepository passTicketRepository, UserAccountRepository userAccountRepository) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.bookingRepository = bookingRepository;
        this.passTicketRepository = passTicketRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("상태가 READY이고 시작일시가 10분 후인 알림 대상을 찾아 notification 추가")
    @Test
    void addNotificationStep() {
        // Given
        addBooking();
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("addNotificationStep");
        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

    }

    @DisplayName("PT 받기 전 알림이 아직 미발송인 경우 사용자에게 알림 전송")
    @Test
    void sendNotificationStep() {
        // Given
        addBooking();
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("sendNotificationStep");
        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    private void addBooking() {
        String userId = "C1000000";
        LocalDateTime now = LocalDateTime.now();
        UserAccount userAccount = UserAccount.of(
                userId,
                "김주영",
                createUserGroup(),
                UserAccountStatus.ACTIVE,
                "01012341234",
                Map.of("uuid", "1234asdf")
        );
        PassTicket passTicket = PassTicket.of(
                1L,
                userId,
                PassTicketStatus.PROGRESSED,
                10,
                now.minusDays(60),
                now.minusDays(1)
        );
        Booking booking = Booking.of(
                passTicket.getPackageId(),
                userAccount,
                BookingStatus.READY,
                now.plusMinutes(10),
                now.plusMinutes(50)
        );

        userAccountRepository.save(userAccount);
        passTicketRepository.save(passTicket);
        bookingRepository.save(booking);
    }

    private UserGroup createUserGroup() {
        return UserGroup.of(
                "TAESAN",
                "태산"
        );
    }
}
