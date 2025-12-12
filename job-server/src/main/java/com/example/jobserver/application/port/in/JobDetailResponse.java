package com.example.jobserver.application.port.in;

import com.example.jobserver.domain.model.Job.JobStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record JobDetailResponse(
        UUID jobId,
        JobStatus status,
        JobResultDto result,
        String errorMessage,
        UserDto user,
        ProjectDto project,
        List<TaskDto> tasks,
        LocalDateTime createdAt,
        LocalDateTime updateAt
) {
}
