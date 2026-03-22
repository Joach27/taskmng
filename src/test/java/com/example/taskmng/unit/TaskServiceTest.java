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
import com.example.taskmng.model.Statut;
import com.example.taskmng.model.Task;

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
    
    // Return all pages of tasks
    @Test
    void getAllTasks_ShouldReturnPagesofTasks() {
        // Arrange (Given)
        Pageable pageable = PageRequest.of(0, 10);
        List<Task> tasks = List.of(new Task(), new Task());
        Page<Task> mockPageOfTasks = new PageImpl<>(tasks, pageable, tasks.size());
        given(taskRepository.findAll(pageable)).willReturn(mockPageOfTasks);
        
        // Act
        Page<Task> result = taskService.getAllTask(0, 10);
        
        // Assert
        assertEquals(2, result.getContent().size());
        verify(taskRepository).findAll(pageable);
    }
    
    // Task exists
    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists(){
        // Arrange (Given)
        Long taskId = 1L;
        Task mockTask = new Task(taskId, "Task 1", "Do it before tomorrow", LocalDate.parse("2026-03-31"), Statut.TODO);
        given(taskRepository.findById(taskId)).willReturn(Optional.of(mockTask));
        
        // Act (When)
        Task result = taskService.getTaskById(taskId);
        
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