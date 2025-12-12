package com.example.jobserver.application.port.in;

import java.util.UUID;

public record JobResultDto(UUID jobId, Integer value, String externalStatus) {
}
