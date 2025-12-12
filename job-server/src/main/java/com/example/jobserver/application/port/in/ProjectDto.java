package com.example.jobserver.application.port.in;

import java.util.UUID;

public record ProjectDto(UUID id, String name, UUID ownerId) {
}
