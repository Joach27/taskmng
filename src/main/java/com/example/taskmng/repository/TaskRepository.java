package com.example.taskmng.repository;

import com.example.taskmng.model.Task;
import com.example.taskmng.model.Statut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByTitre(String titre);
    
    List<Task> findByStatut(Statut statut);
} 