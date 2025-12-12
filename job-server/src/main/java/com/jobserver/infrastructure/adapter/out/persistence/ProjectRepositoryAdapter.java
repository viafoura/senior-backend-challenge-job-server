package com.jobserver.infrastructure.adapter.out.persistence;

import com.jobserver.domain.model.Project;
import com.jobserver.domain.port.out.ProjectRepository;
import com.jobserver.infrastructure.adapter.out.persistence.mapper.ProjectMapper;
import com.jobserver.infrastructure.adapter.out.persistence.repository.JpaProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProjectRepositoryAdapter implements ProjectRepository {

    private final JpaProjectRepository jpaRepo;
    private final ProjectMapper mapper;

    @Override
    public Optional<Project> findById(Long id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepo.existsById(id);
    }
}
