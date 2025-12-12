package com.example.jobserver.application.port.out;

import com.example.jobserver.domain.model.Project;

import java.util.UUID;

public interface ProjectRepository {
    Project findById(UUID id);
    boolean projectExists(UUID id);
}
