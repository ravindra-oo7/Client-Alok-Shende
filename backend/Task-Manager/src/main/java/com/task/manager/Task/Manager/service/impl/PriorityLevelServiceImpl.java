package com.task.manager.Task.Manager.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.manager.Task.Manager.dto.request.PriorityLevelRequestDTO;
import com.task.manager.Task.Manager.dto.response.PriorityLevelResponseDTO;
import com.task.manager.Task.Manager.entity.PriorityLevel;
import com.task.manager.Task.Manager.repository.PriorityLevelRepository;
import com.task.manager.Task.Manager.service.PriorityLevelService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PriorityLevelServiceImpl implements PriorityLevelService {

	private final PriorityLevelRepository priorityLevelRepository;

	@Autowired
	public PriorityLevelServiceImpl(PriorityLevelRepository priorityLevelRepository) {
		this.priorityLevelRepository = priorityLevelRepository;
	}

	// --- Helper Methods (like your convertToResponseDTO) ---

	private PriorityLevelResponseDTO convertToResponseDTO(PriorityLevel priorityLevel) {
		PriorityLevelResponseDTO dto = new PriorityLevelResponseDTO();
		dto.setId(priorityLevel.getId());
		dto.setName(priorityLevel.getName());
		dto.setColorHex(priorityLevel.getColorHex());
		dto.setRank(priorityLevel.getRank());
		dto.setCreatedAt(priorityLevel.getCreatedAt());
		dto.setUpdatedAt(priorityLevel.getUpdatedAt());
		return dto;
	}

	private void checkNameConflict(String name, Long currentId) {
		priorityLevelRepository.findByName(name).ifPresent(existing -> {
			if (currentId == null || !existing.getId().equals(currentId)) {
				throw new IllegalStateException("PriorityLevel name already exists: " + name);
			}
		});
	}

	private void checkRankConflict(Integer rank, Long currentId) {
		priorityLevelRepository.findByRank(rank).ifPresent(existing -> {
			if (currentId == null || !existing.getId().equals(currentId)) {
				throw new IllegalStateException("PriorityLevel rank already exists: " + rank);
			}
		});
	}

	// --- Helper for create and update (PUT) ---
	private void checkForConflicts(String name, Integer rank, Long currentId) {
		checkNameConflict(name, currentId);
		checkRankConflict(rank, currentId);
	}

	// --- Service Method Implementations ---

	@Transactional
	@Override
	public PriorityLevelResponseDTO createPriorityLevel(PriorityLevelRequestDTO requestDTO) {
		// Check for existing unique values
		checkForConflicts(requestDTO.getName(), requestDTO.getRank(), null);

		PriorityLevel priorityLevel = new PriorityLevel();
		priorityLevel.setName(requestDTO.getName());
		priorityLevel.setColorHex(requestDTO.getColorHex());
		priorityLevel.setRank(requestDTO.getRank());

		PriorityLevel savedPriorityLevel = priorityLevelRepository.save(priorityLevel);
		return convertToResponseDTO(savedPriorityLevel);
	}

	@Override
	public PriorityLevelResponseDTO getPriorityLevelById(Long id) {
		PriorityLevel priorityLevel = priorityLevelRepository.findById(id).orElseThrow(() ->
		// Replace with your CustomException
		new EntityNotFoundException("PriorityLevel not found with id: " + id));
		return convertToResponseDTO(priorityLevel);
	}

	@Override
	public List<PriorityLevelResponseDTO> getAllPriorityLevels() {
		return priorityLevelRepository.findAll().stream().map(this::convertToResponseDTO).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public PriorityLevelResponseDTO updatePriorityLevel(Long id, PriorityLevelRequestDTO requestDTO) {
		PriorityLevel existingPriorityLevel = priorityLevelRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("PriorityLevel not found with id: " + id));

		// Check for conflicts if name or rank are being changed
		checkForConflicts(requestDTO.getName(), requestDTO.getRank(), id);

		// This is a PUT operation, so we update all fields
		existingPriorityLevel.setName(requestDTO.getName());
		existingPriorityLevel.setColorHex(requestDTO.getColorHex());
		existingPriorityLevel.setRank(requestDTO.getRank());

		PriorityLevel updatedPriorityLevel = priorityLevelRepository.save(existingPriorityLevel);
		return convertToResponseDTO(updatedPriorityLevel);
	}

	// --- FIX YOUR 'patchUpdatePriorityLevel' METHOD LIKE THIS ---

	@Transactional
	@Override
	public PriorityLevelResponseDTO patchUpdatePriorityLevel(Long id, PriorityLevelRequestDTO requestDTO) {
		PriorityLevel existingPriorityLevel = priorityLevelRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("PriorityLevel not found with id: " + id));

		boolean updated = false;

		// Check and update name
		if (requestDTO.getName() != null && !Objects.equals(requestDTO.getName(), existingPriorityLevel.getName())) {
			checkNameConflict(requestDTO.getName(), id); // Only check name
			existingPriorityLevel.setName(requestDTO.getName());
			updated = true;
		}

		// Check and update colorHex (no conflict check needed)
		if (requestDTO.getColorHex() != null
				&& !Objects.equals(requestDTO.getColorHex(), existingPriorityLevel.getColorHex())) {
			existingPriorityLevel.setColorHex(requestDTO.getColorHex());
			updated = true;
		}

		// Check and update rank
		if (requestDTO.getRank() != null && !Objects.equals(requestDTO.getRank(), existingPriorityLevel.getRank())) {
			checkRankConflict(requestDTO.getRank(), id); // Only check rank
			existingPriorityLevel.setRank(requestDTO.getRank());
			updated = true;
		}

		// Only save if something actually changed
		if (updated) {
			PriorityLevel updatedPriorityLevel = priorityLevelRepository.save(existingPriorityLevel);
			return convertToResponseDTO(updatedPriorityLevel);
		} else {
			// Return the existing one if no changes were made
			return convertToResponseDTO(existingPriorityLevel);
		}
	}

	@Transactional
	@Override
	public void deletePriorityLevel(Long id) {
		PriorityLevel priorityLevel = priorityLevelRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("PriorityLevel not found with id: " + id));
		priorityLevelRepository.delete(priorityLevel);
	}
	
	@Override
    public List<Integer> getUsedRanks() {
        // This calls the new, efficient query you added to the repository
        return priorityLevelRepository.findAllRanks();
    }
}
