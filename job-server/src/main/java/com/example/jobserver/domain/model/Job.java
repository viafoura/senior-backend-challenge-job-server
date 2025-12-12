package com.example.jobserver.domain.model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Job {
    private UUID id;
    private UUID userId;
    private UUID projectId;
    private JobStatus status;
    private JobResult result;
    private String errorMessage;
    private List<Task> tasks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public enum JobStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED
    }

    public Job() {
        id = UUID.randomUUID();
        status = JobStatus.PENDING;
        tasks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }


    public Job(UUID id, UUID userId, UUID projectId, JobStatus status, JobResult result, String errorMessage, List<Task> tasks, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.projectId = projectId;
        this.status = status;
        this.result = result;
        this.errorMessage = errorMessage;
        this.tasks = tasks;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public JobResult getResult() {
        return result;
    }

    public void setResult(JobResult result) {
        this.result = result;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
