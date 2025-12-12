package com.example.jobserver.infrastructure.adapter.out.persistence.entity;

import com.example.jobserver.domain.model.Job.JobStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
public class JobEntity {
    @Id
    @Column(nullable = false)
    private UUID id;
    @Column(name = "user_id", nullable = false)
    private UUID userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;
    @Column(name = "project_id")
    private UUID projectId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private ProjectEntity project;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;
    @Column(name = "result_value")
    private Integer resultValue;
    @Column(name = "result_external_status")
    private String resultExternalStatus;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TaskEntity> tasks = new ArrayList<>();

    public JobEntity() {
    }

    public JobEntity(UUID id, UUID userId, UUID projectId, JobStatus status, Integer resultValue, String resultExternalStatus, String errorMessage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.projectId = projectId;
        this.status = status;
        this.resultValue = resultValue;
        this.resultExternalStatus = resultExternalStatus;
        this.errorMessage = errorMessage;
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public Integer getResultValue() {
        return resultValue;
    }

    public void setResultValue(Integer resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultExternalStatus() {
        return resultExternalStatus;
    }

    public void setResultExternalStatus(String resultExternalStatus) {
        this.resultExternalStatus = resultExternalStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public void addTask(TaskEntity task) {
        tasks.add(task);
        task.setJob(this);
        task.setJobId(this.id);
    }

    public void removeTask(TaskEntity task) {
        tasks.remove(task);
        task.setJob(null);
    }
}
