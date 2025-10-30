package com.task.manager.Task.Manager.repository;

import com.task.manager.Task.Manager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndIsDeletedFalse(Long id);

    List<Task> findByIsDeletedFalse();

    Optional<Task> findByTitleAndIsDeletedFalse(String title);
    
    List<Task> findByIsDeletedTrue();
    
}
