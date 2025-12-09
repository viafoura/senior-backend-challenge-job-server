package com.jobserver.application.service;

import com.jobserver.domain.exception.ExternalServiceException;
import com.jobserver.domain.port.out.ExternalJobProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncJobProcessorTest {

    @Mock
    private JobPersistenceService persistenceService;
    @Mock
    private ExternalJobProcessor externalJobProcessor;

    @InjectMocks
    private AsyncJobProcessor asyncJobProcessor;

    @Test
    void processJob_Success() {
        UUID jobId = UUID.randomUUID();
        Map<String, Object> result = Map.of("value", 42);
        when(externalJobProcessor.process(jobId)).thenReturn(result);

        asyncJobProcessor.processJob(jobId);

        verify(persistenceService).setProcessing(jobId);
        verify(externalJobProcessor).process(jobId);
        verify(persistenceService).setCompleted(jobId, result);
    }

    @Test
    void processJob_ExternalServiceFails_MarksJobFailed() {
        UUID jobId = UUID.randomUUID();
        when(externalJobProcessor.process(jobId)).thenThrow(new ExternalServiceException("timeout"));

        asyncJobProcessor.processJob(jobId);

        verify(persistenceService).setProcessing(jobId);
        verify(persistenceService).setFailed(eq(jobId), contains("External service error"));
    }
}
