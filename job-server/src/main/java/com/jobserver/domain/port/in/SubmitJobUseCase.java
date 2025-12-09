package com.jobserver.domain.port.in;

import com.jobserver.domain.model.Job;

import java.util.Map;

public interface SubmitJobUseCase {
    Job submitJob(Long userId, Long projectId, Map<String, Object> params);
}
