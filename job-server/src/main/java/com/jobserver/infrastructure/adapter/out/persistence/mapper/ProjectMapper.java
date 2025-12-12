package com.jobserver.infrastructure.adapter.out.persistence.mapper;

import com.jobserver.domain.model.Project;
import com.jobserver.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public Project toDomain(ProjectEntity entity) {
        if (entity == null) return null;
        return Project.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .ownerId(entity.getOwnerId())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
