package com.example.jobserver.application.port.in;

import com.example.jobserver.domain.model.Task;

import java.util.UUID;

public record TaskDto(UUID id, String name, Task.TaskStatus status) {
}
