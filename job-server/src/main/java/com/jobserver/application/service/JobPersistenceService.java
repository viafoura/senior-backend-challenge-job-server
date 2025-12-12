package com.jobserver.application.service;

import com.jobserver.domain.model.Job;
import com.jobserver.domain.port.out.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobPersistenceService {

    private final JobRepository jobRepo;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Job> getJob(UUID id) {
        return jobRepo.findById(id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setProcessing(UUID id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found: " + id));
        job.markProcessing();
        jobRepo.save(job);
        log.info("Job {} set to PROCESSING", id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setCompleted(UUID id, Map<String, Object> result) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Job not found: " + id));
        job.markCompleted(result);
        jobRepo.save(job);
        log.info("Job {} completed", id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void setFailed(UUID id, String error) {
        try {
            Job job = jobRepo.findById(id)
                    .orElseThrow(() -> new IllegalStateException("Job not found: " + id));
            job.markFailed(error);
            jobRepo.save(job);
            log.info("Job {} failed", id);
        } catch (Exception e) {
            log.error("Could not update job {} to failed: {}", id, e.getMessage());
        }
    }
}
