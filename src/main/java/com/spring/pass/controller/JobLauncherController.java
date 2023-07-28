package com.spring.pass.controller;

import com.spring.pass.dto.request.JobLauncherRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("job")
public class JobLauncherController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @PostMapping("/launcher")
    public ExitStatus launcherJob(@RequestBody JobLauncherRequest request) throws Exception {
        Job job = jobRegistry.getJob(request.name());
        return jobLauncher.run(job, request.getJobParameters()).getExitStatus();
    }
}
