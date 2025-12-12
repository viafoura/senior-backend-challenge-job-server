package com.example.jobserver.application.port.in;

import java.util.UUID;

public record SubmitJobCommand(UUID userId, UUID projectId) {
    public SubmitJobCommand(UUID userId) {
        this(userId, null);
    }
}
