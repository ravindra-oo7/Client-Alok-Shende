package com.task.manager.Task.Manager.controller;

import com.task.manager.Task.Manager.dto.request.TaskRequestDTO;
import com.task.manager.Task.Manager.dto.response.TaskResponseDTO;
import com.task.manager.Task.Manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(
           @Valid @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.createTask(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> response = taskService.getAllTasks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO response = taskService.getTaskById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id, 
           @Valid @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.updateTask(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> patchUpdateTask(
            @PathVariable Long id, 
            @RequestBody TaskRequestDTO requestDTO) {
        TaskResponseDTO response = taskService.patchUpdateTask(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); 
    }
    
    @GetMapping("/active")
    public ResponseEntity<List<TaskResponseDTO>> getActiveTasks() {
        List<TaskResponseDTO> response = taskService.getActiveTasks();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/deleted")
    public ResponseEntity<List<TaskResponseDTO>> getDeletedTasks() {
        List<TaskResponseDTO> response = taskService.getDeletedTasks();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<TaskResponseDTO> toggleTaskDeleteStatus(@PathVariable Long id) {
        TaskResponseDTO response = taskService.toggleTaskDeleteStatus(id);
        return ResponseEntity.ok(response);
    }
}