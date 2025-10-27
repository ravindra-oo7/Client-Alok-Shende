package com.task.manager.Task.Manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.task.manager.Task.Manager.entity.PriorityLevel;

@Repository
public interface PriorityLevelRepository extends JpaRepository<PriorityLevel, Long> {

    Optional<PriorityLevel> findByName(String name);
    
    Optional<PriorityLevel> findByRank(Integer rank);
    
    @Query("SELECT p.rank FROM PriorityLevel p ORDER BY p.rank ASC")
    List<Integer> findAllRanks();

}