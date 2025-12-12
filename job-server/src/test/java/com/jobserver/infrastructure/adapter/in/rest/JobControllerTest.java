package com.jobserver.infrastructure.adapter.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobserver.domain.exception.JobNotFoundException;
import com.jobserver.domain.exception.UserNotFoundException;
import com.jobserver.domain.model.Job;
import com.jobserver.domain.model.JobStatus;
import com.jobserver.domain.model.User;
import com.jobserver.domain.port.in.GetJobUseCase;
import com.jobserver.domain.port.in.SubmitJobUseCase;
import com.jobserver.infrastructure.adapter.in.rest.dto.SubmitJobRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(JobController.class)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubmitJobUseCase submitJobUseCase;
    @MockBean
    private GetJobUseCase getJobUseCase;

    @Test
    void submitJob_Valid_Returns201() throws Exception {
        UUID jobId = UUID.randomUUID();
        Job job = Job.builder()
                .id(jobId)
                .status(JobStatus.PENDING)
                .userId(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(submitJobUseCase.submitJob(eq(1L), any(), any())).thenReturn(job);

        SubmitJobRequest request = new SubmitJobRequest();
        request.setUserId(1L);
        request.setParameters(Map.of("key", "value"));

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.jobId").value(jobId.toString()))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void submitJob_MissingUserId_Returns400() throws Exception {
        SubmitJobRequest request = new SubmitJobRequest();

        mockMvc.perform(post("/api/v1/jobs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }

    @Test
    void getJob_Exists_Returns200() throws Exception {
        UUID jobId = UUID.randomUUID();
        User user = User.builder().id(1L).username("test").email("test@test.com").build();
        Job job = Job.builder()
                .id(jobId)
                .status(JobStatus.COMPLETED)
                .result(Map.of("value", 42))
                .userId(1L)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        when(getJobUseCase.getJob(jobId)).thenReturn(job);

        mockMvc.perform(get("/api/v1/jobs/{jobId}", jobId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.result.value").value(42));
    }

    @Test
    void getJob_NotFound_Returns404() throws Exception {
        UUID jobId = UUID.randomUUID();
        when(getJobUseCase.getJob(jobId)).thenThrow(new JobNotFoundException(jobId));

        mockMvc.perform(get("/api/v1/jobs/{jobId}", jobId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("JOB_NOT_FOUND"));
    }
}
