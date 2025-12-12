package com.example.jobserver.application.port.out;

import java.util.UUID;

public record ProcessingResult(UUID jobId, Integer value, String status){}