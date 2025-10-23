package com.task.manager.Task.Manager.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task_day_status", uniqueConstraints = {
    // Implements: (task_date, task_id) [unique]
    @UniqueConstraint(
        name = "idx_unique_task_per_day",
        columnNames = {"task_date", "task_id"}
    )
})
public class TaskDayStatus extends BaseEntity {

    @Column(name = "task_date", nullable = false)
    private LocalDate taskDate;

    @Column(name = "is_completed", nullable = false)
    private boolean isCompleted = false; 
    
    @Column(name = "is_active", nullable = false) 
    private boolean isActive = true; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    @ToString.Exclude 
    private Task task;
}