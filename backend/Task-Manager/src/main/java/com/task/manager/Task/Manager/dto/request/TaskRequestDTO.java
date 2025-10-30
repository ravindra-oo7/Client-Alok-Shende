package com.task.manager.Task.Manager.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TaskRequestDTO {

    @NotEmpty(message = "Title cannot be empty")
    @Size(max = 50, message = "Title must be 50 characters or less")
    private String title;

    @NotNull(message = "PriorityLevel ID cannot be null")
    private Long priorityLevelId;

}
