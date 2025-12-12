package com.example.jobserver.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record SubmitJobRequest(UUID userId, UUID projectId) {
}
