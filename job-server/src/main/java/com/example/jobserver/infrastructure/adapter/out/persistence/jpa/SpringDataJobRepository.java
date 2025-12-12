package com.example.jobserver.infrastructure.adapter.out.persistence.jpa;

import com.example.jobserver.infrastructure.adapter.out.persistence.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.jobserver.domain.model.Job.*;

public interface SpringDataJobRepository extends JpaRepository<JobEntity, UUID> {

    @Query("SELECT job FROM JobEntity job LEFT JOIN FETCH job.tasks WHERE job.id = :id")
    JobEntity findByIdWithTasks(UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE JobEntity job SET job.status = :status, job.errorMessage = :errorMessage, job.updatedAt = :updateAt WHERE job.id = :id")
    void updateStatusAndErrorMessage(UUID id, JobStatus status, String errorMessage, LocalDateTime updateAt);
    
    @Modifying
    @Transactional
    @Query("UPDATE JobEntity job SET job.status = :status, job.updatedAt = :updatedAt WHERE job.id = :id")
    void updateStatus(UUID id, JobStatus status, LocalDateTime updatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE JobEntity job SET job.resultValue = :value, job.resultExternalStatus = :externalStatus, job.status = :status, job.updatedAt = :updateAt WHERE job.id = :id")
    void updateResult(UUID id, Integer value, String externalStatus, JobStatus status, LocalDateTime updateAt);
}
