package com.interview.asyncjobserver.ports;

import java.util.UUID;

public interface ExternalProcessor {
    String process(UUID jobId) throws Exception;
}

