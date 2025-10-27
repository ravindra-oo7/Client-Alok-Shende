package com.task.manager.Task.Manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.manager.Task.Manager.dto.request.PriorityLevelRequestDTO;
import com.task.manager.Task.Manager.dto.response.PriorityLevelResponseDTO;
import com.task.manager.Task.Manager.service.PriorityLevelService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/priority-levels") // Standard RESTful naming
public class PriorityLevelController {

    @Autowired
    private PriorityLevelService priorityLevelService;

    @PostMapping
    public ResponseEntity<PriorityLevelResponseDTO> createPriorityLevel(
           @Valid @RequestBody PriorityLevelRequestDTO requestDTO) {
        PriorityLevelResponseDTO response = priorityLevelService.createPriorityLevel(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PriorityLevelResponseDTO>> getAllPriorityLevels() {
        List<PriorityLevelResponseDTO> response = priorityLevelService.getAllPriorityLevels();
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/used-ranks")
    public ResponseEntity<List<Integer>> getUsedRanks() {
        List<Integer> ranks = priorityLevelService.getUsedRanks();
        return ResponseEntity.ok(ranks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PriorityLevelResponseDTO> getPriorityLevelById(@PathVariable Long id) {
        PriorityLevelResponseDTO response = priorityLevelService.getPriorityLevelById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriorityLevelResponseDTO> updatePriorityLevel(
            @PathVariable Long id, 
           @Valid @RequestBody PriorityLevelRequestDTO requestDTO) {
        PriorityLevelResponseDTO response = priorityLevelService.updatePriorityLevel(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PriorityLevelResponseDTO> patchUpdatePriorityLevel(
            @PathVariable Long id, 
            @RequestBody PriorityLevelRequestDTO requestDTO) {
        PriorityLevelResponseDTO response = priorityLevelService.patchUpdatePriorityLevel(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePriorityLevel(@PathVariable Long id) { // Change to ResponseEntity<Void>
        priorityLevelService.deletePriorityLevel(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content
    }
}
