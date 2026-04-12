package com.example.taskmng.unit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;   

import com.example.taskmng.repository.TaskRepository;
import com.example.taskmng.service.TaskService;
import com.example.taskmng.exception.custom.ResourceNotFoundException;
import com.example.taskmng.model.Statut;
import com.example.taskmng.model.Task;
import com.example.taskmng.mapper.*;
import com.example.taskmng.dto.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    
    @InjectMocks
    private TaskService taskService;
    
    @Mock
    private TaskMapper mapper;
    
    // Return all pages of tasks
    @Test
    void getAllTasks_ShouldReturnPagesofTasks() {
        // Arrange (Given)
        Pageable pageable = PageRequest.of(0, 10);
        
        Task task1 = new Task("Task 1", "Desc 1", LocalDate.now(), Statut.TODO);
        Task task2 = new Task("Task 2", "Desc 2", LocalDate.now(), Statut.DONE);
        
        List<Task> tasks = List.of(task1, task2);
        Page<Task> mockPageOfTasks = new PageImpl<>(tasks, pageable, tasks.size());
        
        TaskResponse response1 = new TaskResponse("Task 1", "Desc 1", LocalDate.now(), Statut.TODO);
        TaskResponse response2 = new TaskResponse("Task 2", "Desc 2", LocalDate.now(), Statut.DONE);

        given(taskRepository.findAll(pageable)).willReturn(mockPageOfTasks);
        given(mapper.toResponse(task1)).willReturn(response1);
        given(mapper.toResponse(task2)).willReturn(response2);
        
        // Act
        Page<TaskResponse> result = taskService.getAllTask(0, 10);
        
        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals("Task 1", result.getContent().get(0).getTitre());
        assertEquals("Task 2", result.getContent().get(1).getTitre());
        
        verify(taskRepository).findAll(pageable);
    }
    
    // Task exists
    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists(){
        // Arrange (Given)
        Long taskId = 1L;
        
        Task mockTask = new Task(taskId, "Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        TaskResponse taskResponse = new TaskResponse(taskId, "Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        
        given(mapper.toResponse(mockTask)).willReturn(taskResponse);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(mockTask));
        
        // Act (When)
        TaskResponse result = taskService.getTaskById(taskId);
        
        // Assert (Then)
        assertEquals("Task 1", result.getTitre());
    }
    
    // Task does not exist 
    @Test
    void getTaskById_ShouldTrowException_WhenTaskDoesNotExists(){
        // Arrange 
        Long taskId = 999L;
        given(taskRepository.findById(taskId)).willReturn(Optional.empty());
        
        // Act && Assert
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(taskId)); 
    }
    
    // Creating new task
    @Test
    void createNewTask_ShouldSucced(){
        // Arrange
        Task task = new Task("Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        TaskRequest request = new TaskRequest(
            "Task 1",
            "Do it before tomorrow",
            LocalDate.parse("2026-03-31"),
            Statut.TODO
        );
    
        TaskResponse expectedResponse = new TaskResponse(
            "Task 1",
            "Do it before tomorrow",
            LocalDate.parse("2026-03-31"),
            Statut.TODO
        );
        given(taskRepository.save(task)).willReturn(task);
        given(mapper.toEntity(request)).willReturn(task);
        given(mapper.toResponse(task)).willReturn(expectedResponse);
        
        // Act
        TaskResponse result = taskService.createNewTask(request);
        
        // Assert
        assertThat(result, notNullValue());
        assertThat(result.getTitre(), equalTo("Task 1"));
    }
    
    // Update task
    @Test
    void updateTask_ShouldReturnTask_WithUpdatedFields(){
        // Arrange
        Long taskId = 1L;
        Task existingTask = new Task(
            "Task ancien",
            "Old description",
            LocalDate.parse("2026-03-30"),
            Statut.TODO
        );
    
        TaskRequest updatedRequest = new TaskRequest(
            "Task nouveau",
            "Do it before tomorrow nouveau",
            LocalDate.parse("2026-03-31"),
            Statut.TODO
        );
    
        TaskResponse expectedResponse = new TaskResponse(
            "Task nouveau",
            "Do it before tomorrow nouveau",
            LocalDate.parse("2026-03-31"),
            Statut.TODO
        );
        
        given(taskRepository.findById(taskId)).willReturn(Optional.of(existingTask));
        given(taskRepository.save(any(Task.class))).willReturn(existingTask);
        given(mapper.toResponse(existingTask)).willReturn(expectedResponse);
        
        // Act
        TaskResponse response = taskService.updateTask(taskId, updatedRequest);
        
        // Assert
        assertEquals(("Task nouveau"), response.getTitre());
        assertEquals("Do it before tomorrow nouveau", response.getDescription());
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
    }
    

    // Should throw error when task not found 
    @Test
    void shouldThrowError_WhenTaskNotFound(){
        // Given
        Long id = 999L;
        TaskRequest updatedTask = new TaskRequest(
            "Task ancien",
            "Old description",
            LocalDate.parse("2026-03-30"),
            Statut.TODO
        );
        
        given(taskRepository.findById(id)).willReturn(Optional.empty());
        
        // when && then
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTask(id, updatedTask);
        });
        
        verify(taskRepository).findById(id);
        verify(taskRepository, never()).save(any());
    }
    
    // Delete task
    @Test
    void deleteTask_ShouldNotReturn_TheDeletedTask(){
        // Given
        Task mockTask = new Task(1L, "Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        doNothing().when(taskRepository).deleteById(mockTask.getId());
        
        // When 
        taskService.deleteTask(mockTask.getId());
        
        // Then
        verify(taskRepository, times(1)).deleteById(mockTask.getId());
    }
    
	
}