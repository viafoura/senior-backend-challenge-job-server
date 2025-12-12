package com.example.jobserver.infrastructure.adapter.out.persistence.mapper;

import com.example.jobserver.domain.model.Job;
import com.example.jobserver.domain.model.JobResult;
import com.example.jobserver.domain.model.Task;
import com.example.jobserver.infrastructure.adapter.out.persistence.entity.JobEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobMapper {

    private final TaskMapper taskMapper;

    public JobMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }


    public JobEntity toEntity(Job job) {
        if (job == null) {
            return null;
        }
        Integer resultValue = null;
        String resultExternalStatus = null;
        if (job.getResult() != null) {
            resultValue = job.getResult().getValue();
            resultExternalStatus = job.getResult().getExternalStatus();
        }
        JobEntity jobEntity = new JobEntity(
                job.getId(),
                job.getUserId(),
                job.getProjectId(),
                job.getStatus(),
                resultValue,
                resultExternalStatus,
                job.getErrorMessage(),
                job.getCreatedAt(),
                job.getUpdatedAt()
        );
        if (!job.getTasks().isEmpty()) {
            jobEntity.setTasks(job.getTasks().stream().map(taskMapper::toEntity).toList());
        }
        return jobEntity;
    }

    public Job toDomain(JobEntity jobEntity) {
        if (jobEntity == null) {
            return null;
        }
        JobResult jobResult = null;
        if (jobEntity.getResultValue() != null || jobEntity.getResultExternalStatus() != null) {
            jobResult = new JobResult(
                    jobEntity.getId(),
                    jobEntity.getResultValue(),
                    jobEntity.getResultExternalStatus()
            );
        }

        List<Task> tasks = new ArrayList<>();
        if (jobEntity.getTasks() != null) {
            jobEntity.getTasks().forEach(taskEntity -> tasks.add(taskMapper.toDomain(taskEntity)));
        }

        return new Job(
                jobEntity.getId(),
                jobEntity.getUserId(),
                jobEntity.getProjectId(),
                jobEntity.getStatus(),
                jobResult,
                jobEntity.getErrorMessage(),
                tasks,
                jobEntity.getCreatedAt(),
                jobEntity.getUpdatedAt()
        );
    }
}
