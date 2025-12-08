import com.interview.asyncjobserver.application.JobService;
import com.interview.asyncjobserver.domain.Job;
import com.interview.asyncjobserver.domain.JobStatus;
import com.interview.asyncjobserver.domain.User;
import com.interview.asyncjobserver.dto.JobResponse;
import com.interview.asyncjobserver.ports.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JobServiceRetrieveTest {

    @Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    private Job job;
    UUID jobId = UUID.fromString("ea2af522-7d44-44fa-ba76-a388764e5a94");
    UUID userId = UUID.fromString("0c2329bb-920b-4c21-b2b2-1c8b690b5493");

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setId(userId);
        user.setUsername("alice");

        MockitoAnnotations.openMocks(this);
        job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.PENDING);
        job.setResult("42");
        job.setUser(user);
    }

    @Test
    void testRetrieveJobById() {

        when(jobRepository.findById(any())).thenReturn(Optional.of(job));

        // Call the method
        JobResponse retrievedJob = jobService.getJob(jobId);

        // Verify result
        assertNotNull(retrievedJob);
        assertEquals(jobId, retrievedJob.getJobId());
        assertEquals(JobStatus.PENDING, retrievedJob.getStatus());
        assertEquals("42", retrievedJob.getResult());

        // Verify repository interaction
        verify(jobRepository, times(1)).findById(jobId);
    }

    @Test
    void testRetrieveJobByIdNotFound() {


        when(jobRepository.findById(jobId)).thenReturn(Optional.empty());

        // Check exception
        assertThrows(IllegalArgumentException.class, () -> jobService.getJob(jobId));

        verify(jobRepository, times(1)).findById(jobId);
    }
}
