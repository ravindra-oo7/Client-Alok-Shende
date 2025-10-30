package com.task.manager.Task.Manager.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TaskResponseDTO {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private PriorityLevelResponseDTO priorityLevel;
    
    private boolean isDeleted;
    private LocalDateTime deletedAt;
}
