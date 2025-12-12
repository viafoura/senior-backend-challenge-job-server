package com.example.jobserver.domain.model;

import java.util.Objects;
import java.util.UUID;

public class JobResult {
    private final UUID jobId;
    private final Integer value;
    private final String externalStatus;

    public JobResult(UUID jobId, Integer value, String externalStatus) {
        this.jobId = Objects.requireNonNull(jobId,"Job id must not be null");
        this.value = value;
        this.externalStatus = externalStatus;
    }

    public UUID getJobId() {
        return jobId;
    }

    public Integer getValue() {
        return value;
    }

    public String getExternalStatus() {
        return externalStatus;
    }
}
