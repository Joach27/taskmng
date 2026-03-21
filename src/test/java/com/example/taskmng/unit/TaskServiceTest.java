package com.example.taskmng.unit;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.taskmng.repository.TaskRepository;
import com.example.taskmng.service.TaskService;
import com.example.taskmng.model.Statut;
import com.example.taskmng.model.Task;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    
    @InjectMocks
    private TaskService taskService;
    
    // Task exists
    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists(){
        // Arrange
        Long taskId = 1L;
        Task mockTask = new Task(taskId, "Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        given(taskRepository.findTaskById(taskId)).willReturn(Optional.of(mockTask));
        
        // Act 
        Task result = taskService.getTaskById(taskId);
        
        // Assert
        assertEquals("Task 1", result.getTitre());
    }
    
    // Task does not exist 
    @Test
    void getTaskById_ShouldTrowException_WhenTaskDoesNotExists(){
        // Arrange 
        Long taskId = 999L;
        given(taskRepository.findTaskById(taskId)).willReturn(Optional.empty());
        
        // Act && Assert
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(taskId)); 
    }
    
    // Creating new task
    @Test
    void createNewTask_ShouldSucced(){
        // Arrange
        Task mockTask = new Task("Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        given(taskRepository.save(mockTask)).willReturn(mockTask);
        
        // Act
        Task result = taskService.createNewTask(mockTask);
        
        // Assert
        assertThat(result, notNullValue());
        assertThat(result.getTitre(), equalTo("Task 1"));
    }
    
    // Update task
    @Test
    void updateTask_ShouldReturnTask_WithUpdatedFields(){
        // Arrange
        Long taskId = 1L;
        Task mockTask = new Task(taskId, "Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        String newTitle = "Task 1.1";
        mockTask.setTitre(newTitle);
        given(taskRepository.save(mockTask)).willReturn(mockTask);
 
        // Act
        Task result = taskService.updateTask(taskId, mockTask);
        
        // Assert
        assertEquals(result.getTitre(), mockTask.getTitre());
    }
    
	
}