package com.spring.pass.domain;

import com.spring.pass.domain.constant.NotificationEvent;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "notification") //TODO: Indexing
@Entity
public class Notification extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;

    private NotificationEvent event;
    private String text; //본문
    private boolean sent; //발신 여부

    private LocalDateTime sentAt; //발신 일시

    protected Notification() {
    }

    private Notification(String uuid, NotificationEvent event, String text, boolean sent, LocalDateTime sentAt) {
        this.uuid = uuid;
        this.event = event;
        this.text = text;
        this.sent = sent;
        this.sentAt = sentAt;
    }

    public static Notification of(String uuid, NotificationEvent event, String text, boolean sent, LocalDateTime sentAt) {
        return new Notification(uuid, event, text, sent, sentAt);
    }

    public static Notification fromBooking(Booking booking) {
        return new Notification(
                booking.getUserAccount().getUuid(),
                NotificationEvent.BEFORE_CLASS,
                String.format("안녕하세요. %s 수업 시작합니다. 수업 전 출석 체크 부탁드립니다.", booking.getStartedAt()),
                false,
                null
        );
    }

    public void send() {
        this.sent = true;
        this.sentAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
