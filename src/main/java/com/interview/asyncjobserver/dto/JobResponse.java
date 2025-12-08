package com.interview.asyncjobserver.dto;

import com.interview.asyncjobserver.domain.Job;
import com.interview.asyncjobserver.domain.JobStatus;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Getter
public class JobResponse {
    private UUID jobId;
    private JobStatus status;
    private String result;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID userId;
    private UUID projectId;

    public static JobResponse from(Job job) {
        JobResponse r = new JobResponse();
        r.jobId = job.getId();
        r.status = job.getStatus();
        r.result = job.getResult();
        r.errorMessage = job.getError();
        r.createdAt = job.getCreatedAt();
        r.updatedAt = job.getUpdatedAt();
        r.userId = job.getUser().getId();
        r.projectId = job.getProject() != null ? job.getProject().getId() : null;
        return r;
    }

}
