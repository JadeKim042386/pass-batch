package com.spring.pass.job.notification;

import com.spring.pass.domain.Notification;
import com.spring.pass.repository.NotificationRepository;
import com.spring.pass.service.kakaomessage.KakaoMessageAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class SendNotificationItemWriter implements ItemWriter<Notification> {
    private final NotificationRepository notificationRepository;
    private final KakaoMessageAdaptor kakaoMessageAdaptor;

    @Override
    public void write(List<? extends Notification> notifications) {
        int count = 0;

        for (Notification notification : notifications) {
            boolean successful = kakaoMessageAdaptor.sendKakaoMessage(notification.getUuid(), notification.getText());
            if (successful) {
                notification.send();
                notificationRepository.save(notification);
                count++;
            }
        }
        log.info("SendNotificationItemWriter - write: 수업 전 알람 {}/{}건 전송 성공", count, notifications.size());
    }
}
