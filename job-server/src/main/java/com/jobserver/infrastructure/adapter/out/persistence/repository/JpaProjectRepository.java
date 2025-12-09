package com.jobserver.infrastructure.adapter.out.persistence.repository;

import com.jobserver.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
