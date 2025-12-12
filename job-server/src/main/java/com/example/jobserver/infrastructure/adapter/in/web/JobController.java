package com.example.jobserver.infrastructure.adapter.in.web;

import com.example.jobserver.application.port.in.*;
import com.example.jobserver.infrastructure.adapter.in.web.dto.SubmitJobRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/jobs")
public class JobController {

    private final SubmitJob submitJob;
    private final GetJobStatus getJobStatus;


    public JobController(SubmitJob submitJob, GetJobStatus getJobStatus) {
        this.submitJob = submitJob;
        this.getJobStatus = getJobStatus;
    }

    @PostMapping
    public ResponseEntity<JobResponse> submitJob(@RequestBody SubmitJobRequest request) {
        SubmitJobCommand submitJobCommand = new SubmitJobCommand(request.userId(), request.projectId());
        JobResponse jobResponse = submitJob.submit(submitJobCommand);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jobResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDetailResponse> getJob(@PathVariable("id")UUID jobId) {
        JobDetailResponse jobDetailResponse = getJobStatus.getJobById(jobId);
        return ResponseEntity.ok(jobDetailResponse);
    }
}
