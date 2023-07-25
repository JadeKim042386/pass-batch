package com.spring.pass.domain;

import com.spring.pass.domain.constant.BookingStatus;
import lombok.Getter;
import lombok.ToString;

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
    private Long passId; //이용권 ID
    private String userId; //사용자 ID

    @Enumerated(EnumType.STRING)
    private BookingStatus status; //예약 상태
    private boolean usedPass; //사용 여부
    private boolean attended; //출석 여부

    private LocalDateTime startedAt; //시작 일시
    private LocalDateTime endedAt; //종료 일시
    private LocalDateTime cancelledAt; //취소 일시

    protected Booking() {
    }

    private Booking(Long passId, String userId, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt) {
        this.passId = passId;
        this.userId = userId;
        this.status = status;
        this.usedPass = usedPass;
        this.attended = attended;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.cancelledAt = cancelledAt;
    }

    public static Booking of(Long passId, String userId, BookingStatus status, boolean usedPass, boolean attended, LocalDateTime startedAt, LocalDateTime endedAt, LocalDateTime cancelledAt) {
        return new Booking(passId, userId, status, usedPass, attended, startedAt, endedAt, cancelledAt);
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
