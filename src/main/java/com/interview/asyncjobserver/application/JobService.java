package com.interview.asyncjobserver.application;

import com.interview.asyncjobserver.domain.Job;
import com.interview.asyncjobserver.domain.JobStatus;
import com.interview.asyncjobserver.domain.Project;
import com.interview.asyncjobserver.domain.User;
import com.interview.asyncjobserver.dto.JobRequest;
import com.interview.asyncjobserver.dto.JobResponse;
import com.interview.asyncjobserver.ports.ExternalProcessor;
import com.interview.asyncjobserver.ports.JobRepository;
import com.interview.asyncjobserver.ports.ProjectRepository;
import com.interview.asyncjobserver.ports.UserRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class JobService {

    private final JobRepository jobRepo;
    private final UserRepository userRepo;
    private final ProjectRepository projectRepo;
    private final ExternalProcessor externalProcessor;

    public JobService(JobRepository jobRepo,
                      UserRepository userRepo,
                      ProjectRepository projectRepo,
                      ExternalProcessor externalProcessor) {
        this.jobRepo = jobRepo;
        this.userRepo = userRepo;
        this.projectRepo = projectRepo;
        this.externalProcessor = externalProcessor;
    }


    public UUID submitJob(JobRequest req) {

        User user = userRepo.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid userId"));

        Project project = null;
        if (req.getProjectId() != null) {
            project = projectRepo.findById(req.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid projectId"));
        }

        Job job = Job.builder()
                .status(JobStatus.PENDING)
                .user(user)
                .project(project)
                .createdAt(LocalDateTime.now())
                .build();;

        job = jobRepo.save(job);

        return job.getId();
    }


    public JobResponse getJob(UUID id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));
        return JobResponse.from(job);
    }

    @Async
    public void processAsync(UUID jobId) {
        try {
            Job job = jobRepo.findById(jobId).orElseThrow();
            job.setStatus(JobStatus.PROCESSING);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepo.save(job);

            String result = externalProcessor.process(jobId);

            job.setStatus(JobStatus.COMPLETED);
            job.setResult(result);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepo.save(job);

        } catch (Exception ex) {
            Job job = jobRepo.findById(jobId).orElse(null);
            if (job != null) {
                job.setStatus(JobStatus.FAILED);
                job.setError(ex.getMessage());
                job.setUpdatedAt(LocalDateTime.now());
                jobRepo.save(job);
            }
        }
    }
}

