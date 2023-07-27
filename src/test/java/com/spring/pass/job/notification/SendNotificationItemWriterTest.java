package com.spring.pass.job.notification;

import com.spring.pass.domain.Notification;
import com.spring.pass.domain.constant.NotificationEvent;
import com.spring.pass.repository.NotificationRepository;
import com.spring.pass.service.kakaomessage.KakaoMessageAdaptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@Slf4j
@ExtendWith(MockitoExtension.class)
class SendNotificationItemWriterTest {
    @InjectMocks private SendNotificationItemWriter sut;
    @Mock private NotificationRepository notificationRepository;
    @Mock private KakaoMessageAdaptor kakaoMessageAdaptor;

    @DisplayName("전송한 알림 저장")
    @Test
    void saveSendedNotification() {
        // Given
        Notification notification = createNotification();
        given(kakaoMessageAdaptor.sendKakaoMessage(anyString(), anyString())).willReturn(true);
        given(notificationRepository.save(notification)).willReturn(notification);
        // When
        sut.write(List.of(notification));
        // Then
        then(kakaoMessageAdaptor).should().sendKakaoMessage(anyString(), anyString());
        then(notificationRepository).should().save(notification);

        assertThat(notification.isSent()).isEqualTo(true);
        assertThat(notification.getSentAt()).isNotNull();
    }

    private Notification createNotification() {
        return Notification.of(
                "1234asdf",
                NotificationEvent.BEFORE_CLASS,
                "수업 시작 10분 전",
                false,
                null
        );
    }
}
