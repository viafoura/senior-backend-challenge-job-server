package com.jobserver.application.service;

import com.jobserver.domain.exception.JobNotFoundException;
import com.jobserver.domain.exception.ProjectNotFoundException;
import com.jobserver.domain.exception.UserNotFoundException;
import com.jobserver.domain.model.Job;
import com.jobserver.domain.port.in.GetJobUseCase;
import com.jobserver.domain.port.in.SubmitJobUseCase;
import com.jobserver.domain.port.out.JobRepository;
import com.jobserver.domain.port.out.ProjectRepository;
import com.jobserver.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService implements SubmitJobUseCase, GetJobUseCase {

    private final JobRepository jobRepo;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final AsyncJobProcessor asyncProcessor;

    @Override
    @Transactional
    public Job submitJob(Long userId, Long projectId, Map<String, Object> params) {
        log.info("Submitting job for user: {}", userId);

        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        if (projectId != null && !projectRepo.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        Job job = Job.createPending(userId, projectId, params);
        Job saved = jobRepo.save(job);

        log.info("Created job: {}", saved.getId());

        asyncProcessor.processJob(saved.getId());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Job getJob(UUID jobId) {
        return jobRepo.findByIdWithRelations(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Job> getJobsByUser(Long userId) {
        if (!userRepo.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }
        return jobRepo.findByUserId(userId);
    }
}
