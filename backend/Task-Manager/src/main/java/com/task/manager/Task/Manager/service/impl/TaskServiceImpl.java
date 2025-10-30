package com.task.manager.Task.Manager.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.manager.Task.Manager.dto.request.TaskRequestDTO;
import com.task.manager.Task.Manager.dto.response.PriorityLevelResponseDTO;
import com.task.manager.Task.Manager.dto.response.TaskResponseDTO;
import com.task.manager.Task.Manager.entity.PriorityLevel;
import com.task.manager.Task.Manager.entity.Task;
import com.task.manager.Task.Manager.repository.PriorityLevelRepository;
import com.task.manager.Task.Manager.repository.TaskRepository;
import com.task.manager.Task.Manager.service.TaskService;

// Import your custom exceptions or use a standard one
import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskServiceImpl implements TaskService {

	private final TaskRepository taskRepository;
	private final PriorityLevelRepository priorityLevelRepository; // To link PriorityLevel

	@Autowired
	public TaskServiceImpl(TaskRepository taskRepository, PriorityLevelRepository priorityLevelRepository) {
		this.taskRepository = taskRepository;
		this.priorityLevelRepository = priorityLevelRepository;
	}

	private TaskResponseDTO convertToResponseDTO(Task task) {
		TaskResponseDTO dto = new TaskResponseDTO();
		dto.setId(task.getId());
		dto.setTitle(task.getTitle());
		dto.setCreatedAt(task.getCreatedAt());
		dto.setUpdatedAt(task.getUpdatedAt());

		if (task.getPriorityLevel() != null) {
			dto.setPriorityLevel(convertToPriorityLevelDTO(task.getPriorityLevel()));
		}

		dto.setDeleted(task.isDeleted());
		dto.setDeletedAt(task.getDeletedAt());

		return dto;
	}

	private PriorityLevelResponseDTO convertToPriorityLevelDTO(PriorityLevel priorityLevel) {
		PriorityLevelResponseDTO dto = new PriorityLevelResponseDTO();
		dto.setId(priorityLevel.getId());
		dto.setName(priorityLevel.getName());
		dto.setColorHex(priorityLevel.getColorHex());
		dto.setRank(priorityLevel.getRank());
		dto.setCreatedAt(priorityLevel.getCreatedAt());
		dto.setUpdatedAt(priorityLevel.getUpdatedAt());
		return dto;
	}

	private PriorityLevel getPriorityLevelById(Long id) {
		return priorityLevelRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("PriorityLevel not found with id: " + id));
	}

	@Transactional
	@Override
	public TaskResponseDTO createTask(TaskRequestDTO requestDTO) {
		// Find the associated PriorityLevel
		PriorityLevel priorityLevel = getPriorityLevelById(requestDTO.getPriorityLevelId());

		Task task = new Task();
		task.setTitle(requestDTO.getTitle());
		task.setPriorityLevel(priorityLevel);
		task.setDeleted(false);

		Task savedTask = taskRepository.save(task);
		return convertToResponseDTO(savedTask);
	}

	@Transactional(readOnly = true)
	@Override
	public TaskResponseDTO getTaskById(Long id) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
		return convertToResponseDTO(task);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TaskResponseDTO> getAllTasks() {
		// Use the repository method that finds only non-deleted tasks
		return taskRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public TaskResponseDTO updateTask(Long id, TaskRequestDTO requestDTO) {
		Task existingTask = taskRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

		if (existingTask.isDeleted()) {
			throw new IllegalStateException("Cannot update a deleted task. Please restore it first.");
		}

		PriorityLevel priorityLevel = getPriorityLevelById(requestDTO.getPriorityLevelId());
		existingTask.setTitle(requestDTO.getTitle());
		existingTask.setPriorityLevel(priorityLevel);
		Task updatedTask = taskRepository.save(existingTask);
		return convertToResponseDTO(updatedTask);
	}

	@Transactional
	@Override
	public TaskResponseDTO patchUpdateTask(Long id, TaskRequestDTO requestDTO) {
		// Find the existing task
		Task existingTask = taskRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

		if (existingTask.isDeleted()) {
			throw new IllegalStateException("Cannot patch a deleted task. Please restore it first.");
		}

		boolean updated = false;

		// Check and update title
		if (requestDTO.getTitle() != null && !Objects.equals(requestDTO.getTitle(), existingTask.getTitle())) {
			existingTask.setTitle(requestDTO.getTitle());
			updated = true;
		}

		// Check and update priorityLevel
		if (requestDTO.getPriorityLevelId() != null
				&& !Objects.equals(requestDTO.getPriorityLevelId(), existingTask.getPriorityLevel().getId())) {

			PriorityLevel newPriorityLevel = getPriorityLevelById(requestDTO.getPriorityLevelId());
			existingTask.setPriorityLevel(newPriorityLevel);
			updated = true;
		}

		// Only save to the DB if something actually changed
		if (updated) {
			existingTask = taskRepository.save(existingTask);
		}

		return convertToResponseDTO(existingTask);
	}

	@Transactional
	@Override
	public void deleteTask(Long id) {
		// Find the task
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

		// Check if already deleted to prevent duplicate operations
		if (!task.isDeleted()) {
			task.setDeleted(true);
			task.setDeletedAt(LocalDateTime.now());
			taskRepository.save(task);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public List<TaskResponseDTO> getActiveTasks() {
		// Uses the existing repository method
		return taskRepository.findByIsDeletedFalse().stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<TaskResponseDTO> getDeletedTasks() {
		// Uses the existing repository method
		return taskRepository.findByIsDeletedTrue().stream().map(this::convertToResponseDTO)
				.collect(Collectors.toList());
	}

	@Transactional
	@Override
	public TaskResponseDTO toggleTaskDeleteStatus(Long id) {
		// Finds the task by ID (active or deleted)
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));

		// Toggle the delete status
		boolean newStatus = !task.isDeleted();
		task.setDeleted(newStatus);

		// Update timestamp
		if (newStatus) {
			// Task was just deleted
			task.setDeletedAt(LocalDateTime.now());
		} else {
			// Task was just restored (undeleted)
			task.setDeletedAt(null);
		}

		Task updatedTask = taskRepository.save(task);
		return convertToResponseDTO(updatedTask);
	}

}
