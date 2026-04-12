package com.example.taskmng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import com.example.taskmng.service.TaskService;
import com.example.taskmng.dto.TaskRequest;
import com.example.taskmng.dto.TaskResponse;


@RestController
@RequestMapping("/api/v1")
public class TaskController{
    @Autowired
    private TaskService taskService;
    
    // Lister toutes les taches avec pagination
    @GetMapping("/tasks")
    public Page<TaskResponse> getAllTasks(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "5") int size){
        return taskService.getAllTask(page, size);
    }
    
    @GetMapping("/task/{id}")
    public TaskResponse getTaskById(@PathVariable Long id){
        return taskService.getTaskById(id);
    }
    
    // Create a new task
    @PostMapping("/tasks")
    public TaskResponse createNewTask(@Valid @RequestBody TaskRequest request){
        return taskService.createNewTask(request);            
    }
    
    // Update task
    @PutMapping("/task/{id}") 
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest taskRequest){
        return taskService.updateTask(id, taskRequest);
    }
    
    @DeleteMapping("/task/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}