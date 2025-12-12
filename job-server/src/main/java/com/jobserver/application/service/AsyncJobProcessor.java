package com.jobserver.application.service;

import com.jobserver.domain.exception.ExternalServiceException;
import com.jobserver.domain.port.out.ExternalJobProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncJobProcessor {

    private final JobPersistenceService persistenceService;
    private final ExternalJobProcessor extProcessor;

    @Async("jobExecutor")
    public void processJob(UUID jobId) {
        log.info("Processing job: {}", jobId);

        try {
            persistenceService.setProcessing(jobId);

            Map<String, Object> result = extProcessor.process(jobId);

            persistenceService.setCompleted(jobId, result);
            log.info("Job {} done", jobId);

        } catch (ExternalServiceException e) {
            log.error("External service error for job {}: {}", jobId, e.getMessage());
            persistenceService.setFailed(jobId, "External service error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error processing job {}: {}", jobId, e.getMessage(), e);
            persistenceService.setFailed(jobId, "Processing error: " + e.getMessage());
        }
    }
}
