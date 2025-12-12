package com.example.jobserver.infrastructure.adapter.out.persistence;

import com.example.jobserver.application.port.out.UserRepository;
import com.example.jobserver.domain.model.User;
import com.example.jobserver.infrastructure.adapter.out.persistence.jpa.SpringDataUserRepository;
import com.example.jobserver.infrastructure.adapter.out.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public class JpaUserRepositoryAdapter implements UserRepository {

    private final SpringDataUserRepository springDataUserRepository;
    private final UserMapper userMapper;

    public JpaUserRepositoryAdapter(SpringDataUserRepository springDataUserRepository, UserMapper userMapper) {
        this.springDataUserRepository = springDataUserRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User findById(UUID id) {
        return springDataUserRepository.findById(id).map(userMapper::toDomain).orElseThrow();
    }

    @Override
    public boolean userExists(UUID id) {
        return springDataUserRepository.existsById(id);
    }
}
