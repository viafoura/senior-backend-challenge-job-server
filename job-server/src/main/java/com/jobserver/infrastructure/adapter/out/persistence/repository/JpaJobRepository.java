package com.jobserver.infrastructure.adapter.out.persistence.repository;

import com.jobserver.infrastructure.adapter.out.persistence.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaJobRepository extends JpaRepository<JobEntity, UUID> {

    @Query("SELECT j FROM JobEntity j LEFT JOIN FETCH j.user LEFT JOIN FETCH j.project WHERE j.id = :id")
    Optional<JobEntity> findByIdWithRelations(@Param("id") UUID id);

    List<JobEntity> findByUserId(Long userId);
}
