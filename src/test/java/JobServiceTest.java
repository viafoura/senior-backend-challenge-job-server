import com.interview.asyncjobserver.application.JobService;
import com.interview.asyncjobserver.domain.Job;
import com.interview.asyncjobserver.domain.JobStatus;
import com.interview.asyncjobserver.domain.User;
import com.interview.asyncjobserver.dto.JobRequest;
import com.interview.asyncjobserver.ports.ExternalProcessor;
import com.interview.asyncjobserver.ports.JobRepository;
import com.interview.asyncjobserver.ports.ProjectRepository;
import com.interview.asyncjobserver.ports.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest {

    @Mock
    private UserRepository userRepo;
    @Mock
    private ProjectRepository projectRepo;
    @Mock
    private JobRepository jobRepo;
    @Mock
    private ExternalProcessor externalProcessor;

    @InjectMocks
    private JobService jobService;


    @Test
    void testSubmitJob() {
        UUID userId = UUID.randomUUID();
        JobRequest req = new JobRequest();
        req.setUserId(userId);

        User mockUser = User.builder()
                            .id(userId)
                            .build();


        Job savedJob = new Job();
        savedJob.setId(UUID.randomUUID());
        savedJob.setStatus(JobStatus.PENDING);
        savedJob.setUser(mockUser);

        // mock database interactions
        when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        when(jobRepo.save(any(Job.class))).thenReturn(savedJob);

        UUID returnedJobId = jobService.submitJob(req);

        assertNotNull(returnedJobId);
        assertEquals(savedJob.getId(), returnedJobId);

        verify(jobRepo, times(1)).save(any(Job.class));
        verify(userRepo, times(1)).findById(userId);
    }

    // ====== TEST 2: processAsync() ======
    @Test
    void testProcessAsync() throws Exception {
        UUID jobId = UUID.randomUUID();

        Job job = new Job();
        job.setId(jobId);
        job.setStatus(JobStatus.PENDING);

        when(jobRepo.findById(jobId)).thenReturn(Optional.of(job));
        when(externalProcessor.process(jobId)).thenReturn("OK");

        jobService.processAsync(jobId);

        // After async processing, job should become COMPLETED
        assertEquals(JobStatus.COMPLETED, job.getStatus());
        assertEquals("OK", job.getResult());

        verify(jobRepo, times(2)).save(any(Job.class));  // PROCESSING + COMPLETED
        verify(externalProcessor).process(jobId);
    }


}
