package com.jobserver.infrastructure.adapter.out.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobserver.domain.model.Job;
import com.jobserver.infrastructure.adapter.out.persistence.entity.JobEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JobMapper {

    private final ObjectMapper json;
    private final UserMapper userMapper;
    private final ProjectMapper projMapper;

    public Job toDomain(JobEntity entity) {
        if (entity == null) return null;
        return Job.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .parameters(parseJson(entity.getParameters()))
                .result(parseJson(entity.getResult()))
                .errorMessage(entity.getErrorMessage())
                .userId(entity.getUserId())
                .projectId(entity.getProjectId())
                .user(userMapper.toDomain(entity.getUser()))
                .project(projMapper.toDomain(entity.getProject()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .completedAt(entity.getCompletedAt())
                .build();
    }

    public JobEntity toEntity(Job domain) {
        if (domain == null) return null;
        return JobEntity.builder()
                .id(domain.getId())
                .status(domain.getStatus())
                .parameters(toJson(domain.getParameters()))
                .result(toJson(domain.getResult()))
                .errorMessage(domain.getErrorMessage())
                .userId(domain.getUserId())
                .projectId(domain.getProjectId())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .completedAt(domain.getCompletedAt())
                .build();
    }

    private Map<String, Object> parseJson(String str) {
        if (str == null) return null;
        try {
            return json.readValue(str, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private String toJson(Map<String, Object> map) {
        if (map == null) return null;
        try {
            return json.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
