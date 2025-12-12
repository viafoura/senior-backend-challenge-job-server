package com.example.jobserver.infrastructure.adapter.out.persistence.mapper;

import com.example.jobserver.domain.model.User;
import com.example.jobserver.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        return new User(userEntity.getId(), userEntity.getName());
    }
}
