package com.jobserver.domain.port.out;

import com.jobserver.domain.model.Job;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository {
    Job save(Job job);
    Optional<Job> findById(UUID id);
    Optional<Job> findByIdWithRelations(UUID id);
    List<Job> findByUserId(Long userId);
}
