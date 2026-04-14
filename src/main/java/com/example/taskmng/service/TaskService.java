package com.example.taskmng.service;
import com.example.taskmng.model.Task;
import com.example.taskmng.dto.TaskRequest;
import com.example.taskmng.dto.TaskResponse;
import com.example.taskmng.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import com.example.taskmng.exception.custom.ResourceNotFoundException;
import com.example.taskmng.mapper.TaskMapper;
 


@Service
@Transactional(readOnly = true)
public class TaskService {
    @Autowired
    private TaskRepository taskRepository ;
    
    @Autowired
    private TaskMapper mapper;
    
    // Lister toutes les taches
    public Page<TaskResponse> getAllTask(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks =  taskRepository.findAll(pageable);
        
        Page<TaskResponse> responsePage = tasks.map(mapper::toResponse);
        return responsePage;
    }
    
    // Get task by id
    public TaskResponse getTaskById(Long id){
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tache non trouvée"));
        
        TaskResponse response = mapper.toResponse(task);
        return response;
    }
    
    // Create a new task
    @Transactional
    public TaskResponse createNewTask(TaskRequest request){
        Task task = mapper.toEntity(request);
        taskRepository.save(task);
        
        TaskResponse response = mapper.toResponse(task);
        return response;
    }
    
    // Update task
    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest taskRequest){
        Task taskToUpdate = taskRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Tache non existente !"));
        
        if (taskRequest.getTitre() != null){taskToUpdate.setTitre(taskRequest.getTitre());}
        if (taskRequest.getDescription() != null){taskToUpdate.setDescription(taskRequest.getDescription());}
        if (taskRequest.getDateEcheance() != null){taskToUpdate.setDateEcheance(taskRequest.getDateEcheance());}
        if (taskRequest.getStatut() != null){taskToUpdate.setStatut(taskRequest.getStatut());}
        
        taskRepository.save(taskToUpdate);
        return mapper.toResponse(taskToUpdate);
    }
    
    // Delete task
    @Transactional
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}