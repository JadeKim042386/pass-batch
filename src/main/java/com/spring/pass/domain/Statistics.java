package com.spring.pass.domain;

import com.spring.pass.domain.constant.BookingStatus;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Table(name = "statistics") //TODO: Indexing
@Entity
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime statisticAt; //일 단위

    private int allCount;
    private int attendedCount;
    private int canceledCount;

    protected Statistics() {
    }

    private Statistics(LocalDateTime statisticAt, int allCount, int attendedCount, int canceledCount) {
        this.statisticAt = statisticAt;
        this.allCount = allCount;
        this.attendedCount = attendedCount;
        this.canceledCount = canceledCount;
    }

    public static Statistics of(LocalDateTime statisticAt, int allCount, int attendedCount, int canceledCount) {
        return new Statistics(statisticAt, allCount, attendedCount, canceledCount);
    }

    public static Statistics fromBooking(Booking booking) {
        return Statistics.of(
                booking.getStatisticsAt(),
                1,
                booking.isAttended() ? 1 : 0,
                booking.getStatus().equals(BookingStatus.CANCELLED) ? 1 : 0
        );
    }

    public void add(Booking booking) {
        this.allCount++;
        if (booking.isAttended()) {
            this.attendedCount++;
        }
        if (booking.getStatus().equals(BookingStatus.CANCELLED)) {
            this.canceledCount++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Statistics that)) return false;
        return this.getId() != null && Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
