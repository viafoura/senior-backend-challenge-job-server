package com.example.jobserver.application.port.in;

import java.util.UUID;

public interface GetJobStatus {
    JobDetailResponse getJobById(UUID jobId);
}
