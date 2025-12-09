package com.jobserver.infrastructure.adapter.in.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class SubmitJobRequest {
    @NotNull(message = "User ID is required")
    private Long userId;
    private Long projectId;
    private Map<String, Object> parameters;
}
