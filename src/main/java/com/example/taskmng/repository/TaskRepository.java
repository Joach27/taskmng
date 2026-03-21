package com.example.taskmng.repository;

import com.example.taskmng.model.Task;
import com.example.taskmng.model.Statut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    Optional<List<Task>> findByTitre(String titre);
    Optional<Task> findTaskById(long id);
    
    List<Task> findByStatut(Statut statut);
} 