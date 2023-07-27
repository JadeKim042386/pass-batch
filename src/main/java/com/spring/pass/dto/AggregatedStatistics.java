package com.spring.pass.dto;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class AggregatedStatistics {
    private final LocalDateTime statisticAt;
    private long allCount;
    private long attendedCount;
    private long cancelledCount;

    public AggregatedStatistics(LocalDateTime statisticAt, long allCount, long attendedCount, long cancelledCount) {
        this.statisticAt = statisticAt;
        this.allCount = allCount;
        this.attendedCount = attendedCount;
        this.cancelledCount = cancelledCount;
    }

    public void merge(AggregatedStatistics statistics) {
        this.allCount += statistics.getAllCount();
        this.attendedCount += statistics.getAttendedCount();
        this.cancelledCount += statistics.getCancelledCount();
    }
}
