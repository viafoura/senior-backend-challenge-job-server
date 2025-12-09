package com.jobserver.domain.port.in;

import com.jobserver.domain.model.Job;

import java.util.List;
import java.util.UUID;

public interface GetJobUseCase {
    Job getJob(UUID jobId);
    List<Job> getJobsByUser(Long userId);
}
