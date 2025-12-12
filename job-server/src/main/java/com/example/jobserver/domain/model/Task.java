package com.example.jobserver.domain.model;

import java.util.UUID;

public class Task {
    private UUID id;
    private UUID jobId;
    private String name;
    private TaskStatus status;

    public enum TaskStatus {
        PENDING,
        RUNNING,
        COMPLETED,
        FAILED
    }

    public Task(UUID id, UUID jobId, String name, TaskStatus status) {
        this.id = id;
        this.jobId = jobId;
        this.name = name;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getJobId() {
        return jobId;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
