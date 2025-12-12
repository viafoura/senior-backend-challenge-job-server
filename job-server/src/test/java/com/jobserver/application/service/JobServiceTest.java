package com.jobserver.application.service;

import com.jobserver.domain.exception.JobNotFoundException;
import com.jobserver.domain.exception.UserNotFoundException;
import com.jobserver.domain.model.Job;
import com.jobserver.domain.model.JobStatus;
import com.jobserver.domain.model.User;
import com.jobserver.domain.port.out.JobRepository;
import com.jobserver.domain.port.out.ProjectRepository;
import com.jobserver.domain.port.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {

    @Mock
    private JobRepository jobRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private ProjectRepository projectRepo;
    @Mock
    private AsyncJobProcessor asyncProcessor;

    @InjectMocks
    private JobService jobService;

    private User testUser;
    private Job testJob;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testJob = Job.builder()
                .id(UUID.randomUUID())
                .status(JobStatus.PENDING)
                .userId(1L)
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    void submitJob_ValidUser_ReturnsJob() {
        when(userRepo.existsById(1L)).thenReturn(true);
        when(jobRepo.save(any(Job.class))).thenAnswer(inv -> inv.getArgument(0));

        Job result = jobService.submitJob(1L, null, Map.of("key", "value"));

        assertThat(result.getStatus()).isEqualTo(JobStatus.PENDING);
        assertThat(result.getUserId()).isEqualTo(1L);
        verify(asyncProcessor).processJob(result.getId());
    }

    @Test
    void submitJob_InvalidUser_ThrowsException() {
        when(userRepo.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> jobService.submitJob(999L, null, Map.of()))
                .isInstanceOf(UserNotFoundException.class);

        verify(jobRepo, never()).save(any());
    }

    @Test
    void getJob_Exists_ReturnsJob() {
        UUID jobId = testJob.getId();
        when(jobRepo.findByIdWithRelations(jobId)).thenReturn(Optional.of(testJob));

        Job result = jobService.getJob(jobId);

        assertThat(result.getId()).isEqualTo(jobId);
        assertThat(result.getUser().getUsername()).isEqualTo("testuser");
    }

    @Test
    void getJob_NotFound_ThrowsException() {
        UUID jobId = UUID.randomUUID();
        when(jobRepo.findByIdWithRelations(jobId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jobService.getJob(jobId))
                .isInstanceOf(JobNotFoundException.class);
    }
}
