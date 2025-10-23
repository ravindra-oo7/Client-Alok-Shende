package com.task.manager.Task.Manager.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "priority_level")
public class PriorityLevel extends BaseEntity {

    @Column(name = "name", length = 25, nullable = false, unique = true)
    private String name;

    @Column(name = "color_hex", length = 7)
    private String colorHex;

    @Column(name = "rank", nullable = false, unique = true)
    private Integer rank;

    @OneToMany(mappedBy = "priorityLevel")
    @ToString.Exclude 
    private Set<Task> tasks;
}