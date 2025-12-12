package com.jobserver.infrastructure.adapter.out.persistence;

import com.jobserver.domain.model.Job;
import com.jobserver.domain.port.out.JobRepository;
import com.jobserver.infrastructure.adapter.out.persistence.mapper.JobMapper;
import com.jobserver.infrastructure.adapter.out.persistence.repository.JpaJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JobRepositoryAdapter implements JobRepository {

    private final JpaJobRepository jpaRepo;
    private final JobMapper mapper;

    @Override
    public Job save(Job job) {
        var entity = mapper.toEntity(job);
        var saved = jpaRepo.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Job> findById(UUID id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Job> findByIdWithRelations(UUID id) {
        return jpaRepo.findByIdWithRelations(id).map(mapper::toDomain);
    }

    @Override
    public List<Job> findByUserId(Long userId) {
        return jpaRepo.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
