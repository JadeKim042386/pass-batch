package com.spring.pass.job.statistics;


import com.spring.pass.dto.AggregatedStatistics;
import com.spring.pass.repository.StatisticsRepository;
import com.spring.pass.utils.CustomCSVWriter;
import com.spring.pass.utils.LocalDateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@StepScope
public class MakeDailyStatisticsTasklet implements Tasklet {
    @Value("#{jobParameters[from]}")
    private String fromString;
    @Value("#{jobParameters[to]}")
    private String toString;
    private final StatisticsRepository statisticsRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        LocalDateTime from = LocalDateTimeUtils.parse(fromString);
        LocalDateTime to = LocalDateTimeUtils.parse(toString);

        List<AggregatedStatistics> statistics = statisticsRepository.findByStatisticAtBetweenAndGroupBy(from, to);
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"statisticAt", "allCount", "attendedAt", "canceledCount"});

        for (AggregatedStatistics statistic : statistics) {
            data.add(new String[]{
                    LocalDateTimeUtils.format(statistic.getStatisticAt()),
                    String.valueOf(statistic.getAllCount()),
                    String.valueOf(statistic.getAttendedCount()),
                    String.valueOf(statistic.getCancelledCount())
            });
        }
        //csv 저장
        CustomCSVWriter.write("daily_statistics_"
                        + LocalDateTimeUtils.format(from, LocalDateTimeUtils.YYYY_MM_DD)
                        + ".csv",
                        data);
        return RepeatStatus.FINISHED;
    }
}
