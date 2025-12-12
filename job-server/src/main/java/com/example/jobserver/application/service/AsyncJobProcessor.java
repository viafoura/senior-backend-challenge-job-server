package com.example.jobserver.application.service;

import com.example.jobserver.application.port.out.ExternalJobProcessor;
import com.example.jobserver.application.port.out.JobRepository;
import com.example.jobserver.application.port.out.ProcessingResult;
import com.example.jobserver.domain.exception.ExternalServiceException;
import com.example.jobserver.domain.model.Job;
import com.example.jobserver.domain.model.Job.JobStatus;
import com.example.jobserver.domain.model.JobResult;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AsyncJobProcessor {

    private final JobRepository jobRepository;
    private final ExternalJobProcessor externalJobProcessor;
    private final RetryTemplate retryTemplate;

    public AsyncJobProcessor(JobRepository jobRepository, ExternalJobProcessor externalJobProcessor, RetryTemplate retryTemplate) {
        this.jobRepository = jobRepository;
        this.externalJobProcessor = externalJobProcessor;
        this.retryTemplate = retryTemplate;
    }

    @Async("taskExecutor")
    public void processJob(UUID jobId) {
        try {
            jobRepository.updateStatus(jobId, JobStatus.PROCESSING);

            ProcessingResult processingResult = callExternalServiceWithRetry(jobId);

            JobResult jobResult = new JobResult(
                    processingResult.jobId(),
                    processingResult.value(),
                    processingResult.status()
            );

            jobRepository.updateResult(jobId, jobResult);
            jobRepository.updateStatus(jobId, JobStatus.COMPLETED);
        } catch (Exception e) {
            handleJobFailure(jobId, e);
        }

    }

    private void handleJobFailure(UUID jobId, Exception exception) {
        Job job = jobRepository.findById(jobId);
        job.setErrorMessage(exception.getMessage());
        jobRepository.save(job);
        jobRepository.updateStatus(jobId, JobStatus.FAILED);
    }

    private ProcessingResult callExternalServiceWithRetry(UUID jobId) {
        return retryTemplate.execute(_ -> {
            try {
                return externalJobProcessor.process(jobId);
            } catch (Exception exception) {
                throw new ExternalServiceException("External service call failed: " + exception.getMessage(), exception);
            }
        });
    }
}
