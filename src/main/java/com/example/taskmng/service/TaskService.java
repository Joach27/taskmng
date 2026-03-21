package com.example.taskmng.service;
import com.example.taskmng.model.Task;
import com.example.taskmng.repository.TaskRepository;

import org.springframework.stereotype.Service;
    
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.example.taskmng.exception.custom.ResourceNotFoundException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository ;
    
    // Lister toutes les taches
    public Page<Task> getAllTask(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return taskRepository.findAll(pageable);
    }
    
    // Get task by id
    public Task getTaskById(long id){
        return taskRepository.findTaskById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tache non trouvée"));
    }
    
    // Create a new task
    public Task createNewTask(Task task){
        return taskRepository.save(task);
    }
    
    // Update task
    public Task updateTask(long id, Task task){
        task.setId(id);
        return taskRepository.save(task);
    }
    
    // Delete task
    public void deleteTask(long id){
        taskRepository.deleteById(id);
    }
}