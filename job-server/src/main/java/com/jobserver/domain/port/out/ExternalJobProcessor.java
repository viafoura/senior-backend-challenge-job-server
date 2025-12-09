package com.jobserver.domain.port.out;

import java.util.Map;
import java.util.UUID;

public interface ExternalJobProcessor {
    Map<String, Object> process(UUID jobId);
}
