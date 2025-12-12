package com.example.jobserver.application.port.out;

import com.example.jobserver.domain.model.User;

import java.util.UUID;

public interface UserRepository {
    User findById(UUID id);
    boolean userExists(UUID id);
}
