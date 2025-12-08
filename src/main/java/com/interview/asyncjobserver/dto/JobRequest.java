package com.interview.asyncjobserver.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class JobRequest {
    private UUID userId;
    private UUID projectId;


}
