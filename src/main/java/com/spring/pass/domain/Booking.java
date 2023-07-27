package com.spring.pass.domain;

import com.spring.pass.domain.constant.BookingStatus;
import lombok.Getter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "booking") //TODO: Indexing
@Entity
public class Booking extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "passTicketId")
    private PassTicket passTicket; //이용권 ID

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private UserAccount userAccount; //사용자

    @Enumerated(EnumType.STRING)
    private BookingStatus status; //예약 상태
    private boolean usedPass; //사용 여부
    private boolean attended; //출석 여부

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime startedAt; //시작 일시
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime endedAt; //종료 일시
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime cancelledAt; //취소 일시

    protected Booking() {
    }

    private Booking(PassTicket passTicket, UserAccount userAccount, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt) {
        this.passTicket = passTicket;
        this.userAccount = userAccount;
        this.status = status;
        this.usedPass = usedPass;
        this.attended = attended;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.cancelledAt = cancelledAt;
    }

    public static Booking of(PassTicket passTicket, UserAccount userAccount, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt) {
        return new Booking(passTicket, userAccount, status, usedPass, attended, startedAt, endedAt, cancelledAt);
    }

    public static Booking of(PassTicket passTicket, UserAccount userAccount, BookingStatus status, LocalDateTime startedAt, LocalDateTime endedAt) {
        return Booking.of(passTicket, userAccount, status, false, false, startedAt, endedAt, null);
    }

    /**
     * 이용권 사용 여부(usedPass) = true
     */
    public void usePassTicket() {
        this.usedPass = true;
    }

    /**
     * endedAt (yyyy-MM-HH 00:00:00)
     */
    public LocalDateTime getStatisticsAt() {
        return this.endedAt.withHour(0).withMinute(0).withSecond(0).withNano(0);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
