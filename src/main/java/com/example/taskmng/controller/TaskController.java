package com.example.taskmng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;

import com.example.taskmng.service.TaskService;
import com.example.taskmng.model.Task;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class TaskController{
    @Autowired
    private TaskService taskService;
    
    // Lister toutes les taches avec pagination
    @GetMapping("/tasks")
    public Page<Task> getAllTasks(int page, int size){
        return taskService.getAllTask(page, size);
    }
    
    @GetMapping("/task/{id}")
    public Task getTaskById(@PathVariable long id){
        return taskService.getTaskById(id);
    }
    
    // Create a new task
    @PostMapping
    public Task createNewTask(@RequestBody Task task){
        if (task.getTitre() != null && task.getDateEcheance().isAfter(LocalDate.now())){
            return taskService.createNewTask(task);            
        }else throw new IllegalArgumentException("Titre ou date non valide");
    }
    
    // Update task
    @PutMapping("/task/{id}") 
    public Task updateTask(@RequestParam long id, @RequestBody Task task){
        return taskService.updateTask(id, task);
    }
    
    @DeleteMapping("/task/{id}")
    public void deleteTask(long id){
        taskService.deleteTask(id);
    }
}