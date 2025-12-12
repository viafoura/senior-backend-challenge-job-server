package com.example.jobserver.infrastructure.adapter.out.persistence.jpa;

import com.example.jobserver.infrastructure.adapter.out.persistence.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SpringDataProjectRepository extends JpaRepository<ProjectEntity, UUID> {
}
