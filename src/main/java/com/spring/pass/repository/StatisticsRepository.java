package com.spring.pass.repository;

import com.spring.pass.domain.Statistics;
import com.spring.pass.dto.AggregatedStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    @Query(value = "SELECT new com.spring.pass.dto.AggregatedStatistics(s.statisticAt, SUM(s.allCount), SUM(s.attendedCount), SUM(s.canceledCount)) " +
            "         FROM Statistics s " +
            "        WHERE s.statisticAt BETWEEN :from AND :to " +
            "     GROUP BY s.statisticAt")
    List<AggregatedStatistics> findByStatisticAtBetweenAndGroupBy(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
