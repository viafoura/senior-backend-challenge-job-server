package com.example.jobserver.application.port.in;

import com.example.jobserver.domain.model.Job.JobStatus;

import java.util.UUID;

public record JobResponse(UUID jobId, JobStatus status) {
}
