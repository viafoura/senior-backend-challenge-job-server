package com.example.jobserver.domain.exception;

import java.util.UUID;

public class JobNotFoundException extends RuntimeException {
    private final UUID jobId;
    public JobNotFoundException(UUID jobId) {
        super("Job not found with this id: " + jobId);
        this.jobId = jobId;
    }

    public UUID getJobId() {
        return jobId;
    }
}
