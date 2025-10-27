package com.task.manager.Task.Manager.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PriorityLevelRequestDTO {

    @NotEmpty(message = "Priority name cannot be empty")
    @Size(max = 25, message = "Priority name must be 25 characters or less")
    private String name;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Color must be a valid hex code (e.g., #FF0000)")
    private String colorHex;

    @NotNull(message = "Rank cannot be null")
    private Integer rank;
}
