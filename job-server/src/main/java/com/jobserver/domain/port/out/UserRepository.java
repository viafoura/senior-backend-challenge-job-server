package com.jobserver.domain.port.out;

import com.jobserver.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    boolean existsById(Long id);
}
