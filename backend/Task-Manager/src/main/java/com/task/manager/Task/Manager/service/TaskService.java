package com.task.manager.Task.Manager.service;

import com.task.manager.Task.Manager.dto.request.TaskRequestDTO;
import com.task.manager.Task.Manager.dto.response.TaskResponseDTO;

import java.util.List;

public interface TaskService {

    TaskResponseDTO createTask(TaskRequestDTO requestDTO);

    TaskResponseDTO getTaskById(Long id);

    List<TaskResponseDTO> getAllTasks();

    TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO);

    TaskResponseDTO patchUpdateTask(Long id, TaskRequestDTO requestDTO);

    void deleteTask(Long id);
    
    List<TaskResponseDTO> getActiveTasks();
    
    List<TaskResponseDTO> getDeletedTasks();
    
    TaskResponseDTO toggleTaskDeleteStatus(Long id);

}