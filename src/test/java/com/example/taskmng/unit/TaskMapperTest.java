package com.example.taskmng.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.taskmng.dto.TaskRequest;
import com.example.taskmng.dto.TaskResponse;
import com.example.taskmng.mapper.TaskMapper;
import com.example.taskmng.model.Statut;
import com.example.taskmng.model.Task;

@ExtendWith(MockitoExtension.class)
public class TaskMapperTest {
    
    @InjectMocks
    private TaskMapper mapper;
    
	
    // Convert a taskRequest to Entity
    @Test
    void shouldConvertTaskRequest_To_Task(){
        // Given (arrange)
        TaskRequest request = new TaskRequest("Task 1", "Important task", LocalDate.now(), Statut.TODO);
        
        // When 
        Task response = mapper.toEntity(request);
        
        // Assert
        assertEquals("Task 1", response.getTitre());
        assertEquals("Important task", response.getDescription());
        
    }
    
    @Test 
    void shouldThrowException_WhenRequestIsNull(){
        assertThrows(NullPointerException.class, ()->{
            mapper.toEntity(null);
        });
    }
    
    @Test
    void shouldMap_ToResponse(){
        // Given (Arrange)
        Task task = new Task("Task 1", "Important task", LocalDate.now(), Statut.TODO);
        task.setId(1L);
        
        // When (Act)
        TaskResponse response = mapper.toResponse(task);
        
        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Task 1", response.getTitre());
        assertEquals(Statut.TODO, response.getStatut());
        
    }
}