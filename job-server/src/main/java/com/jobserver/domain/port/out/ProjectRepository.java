package com.jobserver.domain.port.out;

import com.jobserver.domain.model.Project;

import java.util.Optional;

public interface ProjectRepository {
    Optional<Project> findById(Long id);
    boolean existsById(Long id);
}
