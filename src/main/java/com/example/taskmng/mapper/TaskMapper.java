package com.example.taskmng.mapper;

import com.example.taskmng.dto.TaskRequest;
import com.example.taskmng.dto.TaskResponse;
import com.example.taskmng.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    public Task toEntity(TaskRequest request) {
        Task task = new Task();
        task.setTitre(request.getTitre());
        task.setDescription(request.getDescription());
        task.setDateEcheance(request.getDateEcheance());
        task.setStatut(request.getStatut());
        return task;
    }
    
    public TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitre(task.getTitre());
        response.setDescription(task.getDescription());
        response.setDateEcheance(task.getDateEcheance());
        response.setStatut(task.getStatut());
        return response;
    }
}