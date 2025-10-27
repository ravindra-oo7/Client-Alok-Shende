package com.task.manager.Task.Manager.service;

import java.util.List;

import com.task.manager.Task.Manager.dto.request.PriorityLevelRequestDTO;
import com.task.manager.Task.Manager.dto.response.PriorityLevelResponseDTO;

public interface PriorityLevelService {

    PriorityLevelResponseDTO createPriorityLevel(PriorityLevelRequestDTO requestDTO);

    PriorityLevelResponseDTO getPriorityLevelById(Long id);

    List<PriorityLevelResponseDTO> getAllPriorityLevels();

    PriorityLevelResponseDTO updatePriorityLevel(Long id, PriorityLevelRequestDTO requestDTO);

    PriorityLevelResponseDTO patchUpdatePriorityLevel(Long id, PriorityLevelRequestDTO requestDTO);

    void deletePriorityLevel(Long id);
    
    List<Integer> getUsedRanks();

}
