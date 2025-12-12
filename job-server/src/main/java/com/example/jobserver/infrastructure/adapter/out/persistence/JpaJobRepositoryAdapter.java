package com.example.jobserver.infrastructure.adapter.out.persistence;

import com.example.jobserver.application.port.out.JobRepository;
import com.example.jobserver.domain.model.Job;
import com.example.jobserver.domain.model.JobResult;
import com.example.jobserver.infrastructure.adapter.out.persistence.entity.JobEntity;
import com.example.jobserver.infrastructure.adapter.out.persistence.jpa.SpringDataJobRepository;
import com.example.jobserver.infrastructure.adapter.out.persistence.mapper.JobMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.jobserver.domain.model.Job.*;

@Repository
public class JpaJobRepositoryAdapter implements JobRepository {

    private final SpringDataJobRepository springDataJobRepository;
    private final JobMapper jobMapper;

    public JpaJobRepositoryAdapter(SpringDataJobRepository springDataJobRepository, JobMapper jobMapper) {
        this.springDataJobRepository = springDataJobRepository;
        this.jobMapper = jobMapper;
    }

    @Override
    public Job save(Job job) {
        JobEntity entity = jobMapper.toEntity(job);
        JobEntity saveEntity = springDataJobRepository.save(entity);
        return jobMapper.toDomain(saveEntity);
    }

    @Override
    public Job findById(UUID id) {
        JobEntity jobEntity = springDataJobRepository.findByIdWithTasks(id);
        return jobMapper.toDomain(jobEntity);
    }

    @Override
    public void updateStatus(UUID id, JobStatus status) {
        springDataJobRepository.updateStatus(id, status, LocalDateTime.now());
    }

    @Override
    public void updateResult(UUID id, JobResult result) {
        springDataJobRepository.updateResult(id, result.getValue(), result.getExternalStatus(), JobStatus.COMPLETED, LocalDateTime.now());
    }
}
