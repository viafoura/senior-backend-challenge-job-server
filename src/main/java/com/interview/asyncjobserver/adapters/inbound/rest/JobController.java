package com.interview.asyncjobserver.adapters.inbound.rest;

import com.interview.asyncjobserver.application.JobService;
import com.interview.asyncjobserver.dto.JobRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }


    @PostMapping
    public ResponseEntity<?> submitJob(@RequestBody JobRequest jobRequest) {

        UUID jobId = jobService.submitJob(jobRequest);

        jobService.processAsync(jobId);

        return ResponseEntity.ok(Map.of(
                "jobId", jobId.toString(),
                "status", "PENDING"
        ));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getJob(@PathVariable UUID id) {
        return ResponseEntity.ok(jobService.getJob(id));
    }
}

