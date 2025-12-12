package com.jobserver.infrastructure.adapter.out.persistence.repository;

import com.jobserver.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, Long> {
}
