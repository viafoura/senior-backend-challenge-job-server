package com.jobserver.infrastructure.adapter.in.rest;

import com.jobserver.domain.model.Job;
import com.jobserver.domain.port.in.GetJobUseCase;
import com.jobserver.domain.port.in.SubmitJobUseCase;
import com.jobserver.infrastructure.adapter.in.rest.dto.JobResponse;
import com.jobserver.infrastructure.adapter.in.rest.dto.SubmitJobRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final SubmitJobUseCase submitUseCase;
    private final GetJobUseCase getUseCase;

    @PostMapping
    public ResponseEntity<JobResponse> submit(@Valid @RequestBody SubmitJobRequest req) {
        log.info("Job submission for user: {}", req.getUserId());

        Job job = submitUseCase.submitJob(
                req.getUserId(),
                req.getProjectId(),
                req.getParameters()
        );

        JobResponse resp = JobResponse.builder()
                .jobId(job.getId())
                .status(job.getStatus())
                .parameters(job.getParameters())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @GetMapping("/{jobId}")
    public ResponseEntity<JobResponse> get(@PathVariable UUID jobId) {
        Job job = getUseCase.getJob(jobId);
        return ResponseEntity.ok(JobResponse.fromDomain(job));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<JobResponse>> getByUser(@PathVariable Long userId) {
        List<JobResponse> jobs = getUseCase.getJobsByUser(userId).stream()
                .map(JobResponse::fromDomain)
                .toList();
        return ResponseEntity.ok(jobs);
    }
}
