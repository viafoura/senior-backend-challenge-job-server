package com.jobserver.infrastructure.adapter.in.rest.dto;

import com.jobserver.domain.model.Job;
import com.jobserver.domain.model.JobStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
public class JobResponse {
    private UUID jobId;
    private JobStatus status;
    private Map<String, Object> parameters;
    private Map<String, Object> result;
    private String errorMessage;
    private UserInfo user;
    private ProjectInfo project;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime completedAt;

    @Data
    @Builder
    public static class UserInfo {
        private Long id;
        private String username;
        private String email;
    }

    @Data
    @Builder
    public static class ProjectInfo {
        private Long id;
        private String name;
        private String description;
    }

    public static JobResponse fromDomain(Job j) {
        var b = JobResponse.builder()
                .jobId(j.getId())
                .status(j.getStatus())
                .parameters(j.getParameters())
                .result(j.getResult())
                .errorMessage(j.getErrorMessage())
                .createdAt(j.getCreatedAt())
                .updatedAt(j.getUpdatedAt())
                .completedAt(j.getCompletedAt());

        if (j.getUser() != null) {
            b.user(UserInfo.builder()
                    .id(j.getUser().getId())
                    .username(j.getUser().getUsername())
                    .email(j.getUser().getEmail())
                    .build());
        }

        if (j.getProject() != null) {
            b.project(ProjectInfo.builder()
                    .id(j.getProject().getId())
                    .name(j.getProject().getName())
                    .description(j.getProject().getDescription())
                    .build());
        }

        return b.build();
    }
}
