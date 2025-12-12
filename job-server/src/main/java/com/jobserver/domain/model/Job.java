package com.jobserver.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    private UUID id;
    private JobStatus status;
    private Map<String, Object> parameters;
    private Map<String, Object> result;
    private String errorMessage;
    private Long userId;
    private Long projectId;
    private User user;
    private Project project;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    public static Job createPending(Long userId, Long projectId, Map<String, Object> params) {
        return Job.builder()
                .id(UUID.randomUUID())
                .status(JobStatus.PENDING)
                .parameters(params)
                .userId(userId)
                .projectId(projectId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void markProcessing() {
        this.status = JobStatus.PROCESSING;
        this.updatedAt = LocalDateTime.now();
    }

    public void markCompleted(Map<String, Object> res) {
        this.status = JobStatus.COMPLETED;
        this.result = res;
        this.updatedAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }

    public void markFailed(String error) {
        this.status = JobStatus.FAILED;
        this.errorMessage = error;
        this.updatedAt = LocalDateTime.now();
        this.completedAt = LocalDateTime.now();
    }
}
