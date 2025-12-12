package com.example.jobserver.application.port.out;

import java.util.UUID;

public interface ExternalJobProcessor {
    ProcessingResult process(UUID jobId);
}
