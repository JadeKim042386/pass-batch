package com.spring.pass.job.statistics;

import com.spring.pass.config.TestBatchConfig;
import com.spring.pass.config.TestJpaConfig;
import com.spring.pass.repository.StatisticsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBatchTest
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        MakeStatisticsJobConfig.class,
        TestBatchConfig.class,
        TestJpaConfig.class
})
class MakeStatisticsJobConfigTest {
    private JobLauncherTestUtils jobLauncherTestUtils;
    private StatisticsRepository statisticsRepository;
    @MockBean private MakeDailyStatisticsTasklet makeDailyStatisticsTasklet;
    @MockBean private MakeWeeklyStatisticsTasklet makeWeeklyStatisticsTasklet;

    @Autowired
    public MakeStatisticsJobConfigTest(JobLauncherTestUtils jobLauncherTestUtils, StatisticsRepository statisticsRepository) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.statisticsRepository = statisticsRepository;
    }

    @DisplayName("통계 생성 Job 테스트")
    @Test
    void makeStatisticJobTest() throws Exception {
        // Given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("from", "2022-08-15 00:00")
                .addString("to", "2022-08-16 00:00")
                .toJobParameters();
        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        // Then
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        assertThat(statisticsRepository.findAll()).hasSize(1);
    }
}
