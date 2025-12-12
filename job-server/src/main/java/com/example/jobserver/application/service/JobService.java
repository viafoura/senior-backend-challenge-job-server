package com.example.jobserver.application.service;

import com.example.jobserver.application.port.in.*;
import com.example.jobserver.application.port.out.JobRepository;
import com.example.jobserver.application.port.out.ProjectRepository;
import com.example.jobserver.application.port.out.UserRepository;
import com.example.jobserver.domain.exception.UserNotFoundException;
import com.example.jobserver.domain.model.Job;
import com.example.jobserver.domain.model.Project;
import com.example.jobserver.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.example.jobserver.domain.model.Job.JobStatus;

@Service
public class JobService implements SubmitJob, GetJobStatus {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final AsyncJobProcessor jobProcessor;

    public JobService(JobRepository jobRepository, UserRepository userRepository, ProjectRepository projectRepository, AsyncJobProcessor jobProcessor) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.jobProcessor = jobProcessor;
    }


    @Override
    public JobDetailResponse getJobById(UUID jobId) {
        Job job = jobRepository.findById(jobId);
        User user = userRepository.findById(job.getUserId());
        UserDto userDto = user != null ? new UserDto(user.getId(), user.getName()) : null;
        ProjectDto projectDto = null;
        if (job.getProjectId() != null) {
            Project project = projectRepository.findById(job.getProjectId());
            if (project != null) {
                projectDto = new ProjectDto(project.getId(), project.getName(), project.getOwnerId());
            }
        }

        JobResultDto jobResultDto = job.getResult() != null
                ? new JobResultDto(
                    job.getResult().getJobId(),
                    job.getResult().getValue(),
                    job.getResult().getExternalStatus())
                : null;

        List<TaskDto> taskDtos = job.getTasks().stream().map(task -> new TaskDto(task.getId(), task.getName(), task.getStatus())).toList();
        return new JobDetailResponse(
                job.getId(),
                job.getStatus(),
                jobResultDto,
                job.getErrorMessage(),
                userDto,
                projectDto,
                taskDtos,
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
    }

    @Override
    public JobResponse submit(SubmitJobCommand command) {
        if (command.userId() == null) {
            throw new IllegalArgumentException("User id must not be null");
        }
        if (!userRepository.userExists(command.userId())) {
            throw new UserNotFoundException(command.userId());
        }

        Job job = new Job();
        job.setUserId(command.userId());
        job.setProjectId(command.projectId());
        job.setStatus(JobStatus.PENDING);

        Job savedJob = jobRepository.save(job);

        processJobAsync(savedJob.getId());

        return new JobResponse(savedJob.getId(), savedJob.getStatus());
    }

    private void processJobAsync(UUID jobId) {
        jobProcessor.processJob(jobId);
    }
}
