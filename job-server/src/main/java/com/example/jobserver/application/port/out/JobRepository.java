package com.example.jobserver.application.port.out;

import com.example.jobserver.domain.model.Job;
import com.example.jobserver.domain.model.Job.JobStatus;
import com.example.jobserver.domain.model.JobResult;

import java.util.UUID;

public interface JobRepository {
    Job save(Job job);
    Job findById(UUID id);
    void updateStatus(UUID id, JobStatus status);
    void updateResult(UUID id, JobResult result);
}
