package com.example.jobserver.application.service;

import com.example.jobserver.application.port.in.JobDetailResponse;
import com.example.jobserver.application.port.out.JobRepository;
import com.example.jobserver.application.port.out.ProjectRepository;
import com.example.jobserver.application.port.out.UserRepository;
import com.example.jobserver.domain.model.Job;
import com.example.jobserver.domain.model.Job.JobStatus;
import com.example.jobserver.domain.model.JobResult;
import com.example.jobserver.domain.model.Project;
import com.example.jobserver.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceGetJobTest {

    @Mock
    private JobRepository jobRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private AsyncJobProcessor asyncJobProcessor;

    private JobService jobService;

    @BeforeEach
    void setUp() {
        jobService = new JobService(jobRepository, userRepository, projectRepository, asyncJobProcessor);
    }

    @Test
    void getJobById_withCompletedJob_returnsJobWithResult() {
        // Given
        UUID jobId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        JobResult result = new JobResult(jobId, 42, "DONE");
        Job job = new Job(jobId, userId, null, JobStatus.COMPLETED, result, null, new ArrayList<>(), now, now);
        User user = new User(userId, "Test User");

        when(jobRepository.findById(jobId)).thenReturn(job);
        when(userRepository.findById(userId)).thenReturn(user);

        // When
        JobDetailResponse response = jobService.getJobById(jobId);

        // Then
        assertNotNull(response);
        assertEquals(jobId, response.jobId());
        assertEquals(JobStatus.COMPLETED, response.status());
        assertNotNull(response.result());
        assertEquals(42, response.result().value());
        assertEquals("DONE", response.result().externalStatus());
        assertNull(response.errorMessage());
        assertNotNull(response.user());
        assertEquals("Test User", response.user().name());
    }

    @Test
    void getJobById_withPendingJob_returnsJobWithoutResult() {
        // Given
        UUID jobId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        Job job = new Job(jobId, userId, null, JobStatus.PENDING, null, null, new ArrayList<>(), now, now);
        User user = new User(userId, "Test User");

        when(jobRepository.findById(jobId)).thenReturn(job);
        when(userRepository.findById(userId)).thenReturn(user);

        // When
        JobDetailResponse response = jobService.getJobById(jobId);

        // Then
        assertNotNull(response);
        assertEquals(JobStatus.PENDING, response.status());
        assertNull(response.result());
    }

    @Test
    void getJobById_withFailedJob_returnsJobWithErrorMessage() {
        // Given
        UUID jobId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        Job job = new Job(jobId, userId, null, JobStatus.FAILED, null, "Connection timeout", new ArrayList<>(), now, now);
        User user = new User(userId, "Test User");

        when(jobRepository.findById(jobId)).thenReturn(job);
        when(userRepository.findById(userId)).thenReturn(user);

        // When
        JobDetailResponse response = jobService.getJobById(jobId);

        // Then
        assertNotNull(response);
        assertEquals(JobStatus.FAILED, response.status());
        assertEquals("Connection timeout", response.errorMessage());
    }

    @Test
    void getJobById_withProject_returnsJobWithProjectInfo() {
        // Given
        UUID jobId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        
        Job job = new Job(jobId, userId, projectId, JobStatus.COMPLETED, null, null, new ArrayList<>(), now, now);
        User user = new User(userId, "Test User");
        Project project = new Project(projectId, "Test Project", userId);

        when(jobRepository.findById(jobId)).thenReturn(job);
        when(userRepository.findById(userId)).thenReturn(user);
        when(projectRepository.findById(projectId)).thenReturn(project);

        // When
        JobDetailResponse response = jobService.getJobById(jobId);

        // Then
        assertNotNull(response);
        assertNotNull(response.project());
        assertEquals("Test Project", response.project().name());
    }
}
