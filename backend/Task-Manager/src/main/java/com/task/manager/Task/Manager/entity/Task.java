package com.task.manager.Task.Manager.entity;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(name = "title", length = 50, nullable = false)
    private String title;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false; 

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "priority_level_id")
    @ToString.Exclude
    private PriorityLevel priorityLevel;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude 
    private Set<TaskDayStatus> dailyStatuses;
}