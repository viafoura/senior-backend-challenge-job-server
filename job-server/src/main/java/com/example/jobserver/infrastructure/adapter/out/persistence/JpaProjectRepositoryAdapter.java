package com.example.jobserver.infrastructure.adapter.out.persistence;

import com.example.jobserver.application.port.out.ProjectRepository;
import com.example.jobserver.domain.model.Project;
import com.example.jobserver.infrastructure.adapter.out.persistence.jpa.SpringDataProjectRepository;
import com.example.jobserver.infrastructure.adapter.out.persistence.mapper.ProjectMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaProjectRepositoryAdapter implements ProjectRepository {
    private final SpringDataProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    public JpaProjectRepositoryAdapter(SpringDataProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public Project findById(UUID id) {
        return projectRepository.findById(id).map(projectMapper::toDomain).orElseThrow();
    }

    @Override
    public boolean projectExists(UUID id) {
        return projectRepository.existsById(id);
    }
}
