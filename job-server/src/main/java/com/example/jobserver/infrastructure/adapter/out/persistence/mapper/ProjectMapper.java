package com.example.jobserver.infrastructure.adapter.out.persistence.mapper;

import com.example.jobserver.domain.model.Project;
import com.example.jobserver.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public Project toDomain(ProjectEntity projectEntity) {
        if (projectEntity == null) {
            return null;
        }
        return new Project(projectEntity.getId(), projectEntity.getName(), projectEntity.getOwnerId());
    }
}
