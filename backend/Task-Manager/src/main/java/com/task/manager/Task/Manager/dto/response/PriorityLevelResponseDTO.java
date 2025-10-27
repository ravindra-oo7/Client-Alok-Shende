package com.task.manager.Task.Manager.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PriorityLevelResponseDTO {

    private Long id;
    private String name;
    private String colorHex;
    private Integer rank;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
