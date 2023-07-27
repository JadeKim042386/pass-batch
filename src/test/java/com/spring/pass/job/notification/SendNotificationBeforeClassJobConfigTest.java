package com.spring.pass.job.notification;

import com.spring.pass.config.TestBatchConfig;
import com.spring.pass.config.TestJpaConfig;
import com.spring.pass.domain.*;
import com.spring.pass.domain.constant.BookingStatus;
import com.spring.pass.domain.constant.NotificationEvent;
import com.spring.pass.domain.constant.PassTicketStatus;
import com.spring.pass.domain.constant.UserAccountStatus;
import com.spring.pass.repository.BookingRepository;
import com.spring.pass.repository.NotificationRepository;
import io.hypersistence.utils.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

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
    private NotificationRepository notificationRepository;

    @MockBean private SendNotificationItemWriter sendNotificationItemWriter;

    @Autowired
    public SendNotificationBeforeClassJobConfigTest(
            JobLauncherTestUtils jobLauncherTestUtils,
            BookingRepository bookingRepository,
            NotificationRepository notificationRepository
    ) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.bookingRepository = bookingRepository;
        this.notificationRepository = notificationRepository;
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

        assertThat(notificationRepository.findAll()).hasSize(1);
    }

    @DisplayName("PT 받기 전 알림이 아직 미발송인 경우 사용자에게 알림 전송")
    @Test
    void sendNotificationStep() {
        // Given

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("sendNotificationStep");

        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
    }

    private void addBooking() {
        LocalDateTime now = LocalDateTime.now();
        Booking booking = Booking.of(
                createPassTicket(),
                createUserAccount(),
                BookingStatus.READY,
                now.minusMinutes(10),
                now.plusHours(1)
        );
        bookingRepository.save(booking);
    }

    private PassTicket createPassTicket() {
        PassTicket passTicket = PassTicket.of(
                1L,
                "A1000000",
                PassTicketStatus.PROGRESSED,
                100,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10)
        );
        ReflectionTestUtils.setField(passTicket, "id", 1L);
        return passTicket;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "A1000000",
                "김주영",
                createUserGroup("GROUP", "그룹"),
                UserAccountStatus.ACTIVE,
                "01012341234",
                JacksonUtil.toJsonNode("""
                        {
                            "uuid": "1234asdf"
                        }
                        """)
        );
    }

    private UserGroup createUserGroup(String userGroupId, String userGroupName) {
        return UserGroup.of(
                userGroupId,
                userGroupName,
                userGroupName
        );
    }
}
