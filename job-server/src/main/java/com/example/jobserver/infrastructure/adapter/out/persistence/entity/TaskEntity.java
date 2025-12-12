package com.example.jobserver.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;

import java.util.UUID;

import static com.example.jobserver.domain.model.Task.*;

@Entity
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(name = "job_id", nullable = false)
    private UUID jobId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobEntity job;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    public TaskEntity() {
    }

    public TaskEntity(UUID id, UUID jobId, String name, TaskStatus status) {
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

    public JobEntity getJob() {
        return job;
    }

    public void setJob(JobEntity job) {
        this.job = job;
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