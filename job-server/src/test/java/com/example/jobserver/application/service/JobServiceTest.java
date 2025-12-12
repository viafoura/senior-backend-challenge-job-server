package com.example.jobserver.application.service;

import com.example.jobserver.application.port.in.JobDetailResponse;
import com.example.jobserver.application.port.in.JobResponse;
import com.example.jobserver.application.port.in.SubmitJobCommand;
import com.example.jobserver.application.port.out.JobRepository;
import com.example.jobserver.application.port.out.ProjectRepository;
import com.example.jobserver.application.port.out.UserRepository;
import com.example.jobserver.domain.exception.UserNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

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
    void submitJob_withValidUser_returnsJobWithPendingStatus() {
        // Given
        UUID userId = UUID.randomUUID();
        SubmitJobCommand command = new SubmitJobCommand(userId, null);
        
        when(userRepository.userExists(userId)).thenReturn(true);
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        JobResponse response = jobService.submit(command);

        // Then
        assertNotNull(response);
        assertNotNull(response.jobId());
        assertEquals(JobStatus.PENDING, response.status());
        verify(jobRepository).save(any(Job.class));
        verify(asyncJobProcessor).processJob(any(UUID.class));
    }

    @Test
    void submitJob_withValidUserAndProject_returnsJobWithPendingStatus() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        SubmitJobCommand command = new SubmitJobCommand(userId, projectId);
        
        when(userRepository.userExists(userId)).thenReturn(true);
        when(jobRepository.save(any(Job.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        JobResponse response = jobService.submit(command);

        // Then
        assertNotNull(response);
        assertEquals(JobStatus.PENDING, response.status());
    }

    @Test
    void submitJob_withInvalidUser_throwsUserNotFoundException() {
        // Given
        UUID userId = UUID.randomUUID();
        SubmitJobCommand command = new SubmitJobCommand(userId, null);
        
        when(userRepository.userExists(userId)).thenReturn(false);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> jobService.submit(command));
        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void submitJob_withNullUserId_throwsIllegalArgumentException() {
        // Given
        SubmitJobCommand command = new SubmitJobCommand(null, null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> jobService.submit(command));
    }
}
