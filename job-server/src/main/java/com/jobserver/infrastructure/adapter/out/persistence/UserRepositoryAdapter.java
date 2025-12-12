package com.jobserver.infrastructure.adapter.out.persistence;

import com.jobserver.domain.model.User;
import com.jobserver.domain.port.out.UserRepository;
import com.jobserver.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.jobserver.infrastructure.adapter.out.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepo;
    private final UserMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        return jpaRepo.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepo.existsById(id);
    }
}
