package com.example.jobserver.infrastructure.adapter.out.persistence.mapper;

import com.example.jobserver.domain.model.Task;
import com.example.jobserver.infrastructure.adapter.out.persistence.entity.TaskEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toDomain(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return null;
        }
        return new Task(
                taskEntity.getId(),
                taskEntity.getJobId(),
                taskEntity.getName(),
                taskEntity.getStatus()
        );
    }

    public TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        return new TaskEntity(
                task.getId(),
                task.getJobId(),
                task.getName(),
                task.getStatus()
        );
    }
}
