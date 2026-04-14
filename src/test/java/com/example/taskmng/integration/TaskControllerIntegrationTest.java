package com.example.taskmng.integration;

// import static org.hamcrest.MatcherAssert.assertThat;
// import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.example.taskmng.dto.TaskRequest;
import com.example.taskmng.model.Statut;
import com.example.taskmng.model.Task;
import com.example.taskmng.repository.TaskRepository;

@SpringBootTest
@AutoConfigureMockMvc                   // Pour tester les endpoints HTTP
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Simuler les requêtes HTTP
    
    @Autowired 
    private TaskRepository taskRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private String asJsonString(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    
    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskRepository.save(new Task());
        taskRepository.save(new Task());
    }
    
    // Getting all tasks
    @Test
    void getAllTasks_ShouldReturnPageOfTasks() throws Exception {
        // When && then
        mockMvc.perform(get("/api/v1/tasks")
            .param("page", "0")
            .param("size", "5"))
            .andExpect(status().isOk())
            // .andDo(print())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.totalElements").value(2));
    }
    
    // Get task by id
    @Test
    void getTaskById_ShouldReturnTask() throws Exception{
        // Given (arrange)
        Task mockTask = new Task();
        Task savedTask = taskRepository.save(mockTask);
        
        // When (act) && Then (assert)
        mockMvc.perform(get("/api/v1/tasks/{id}", savedTask.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titre").value("Default title"));
    }
    
    // Task by id not found
    @Test
    void shouldThrow_WhenTaskNotFound() throws Exception{
        mockMvc.perform(get("/api/v1/tasks/{id}", 999L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detail").value("Tache non trouvée"));
    }
    
    // Create new task
    @Test
    void createTask_ShouldRetrunCreatedTask() throws Exception{
        TaskRequest request = new TaskRequest("Task 1", "Important task", LocalDate.now().plusDays(1), Statut.TODO);
        
        mockMvc.perform(post("/api/v1/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titre").value("Task 1"))
            .andExpect(jsonPath("$.id").exists());
    }
    
    // Update task
    @Test
    void updateTask_shouldReturnUpdatedTask() throws Exception{
        // Arrange
        Task existing = taskRepository.save(new Task("Ancien titre", "Important task", LocalDate.now().plusDays(1), Statut.TODO));
        Long id = existing.getId();
        
        TaskRequest updateRequest = new TaskRequest("Nouveau titre", "Important task nouveau", LocalDate.now().plusDays(2), Statut.IN_PROGRESS);
        
        // Act & Assert
        mockMvc.perform(put("/api/v1/tasks/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titre").value("Nouveau titre"));
    }
    
    // Delete task
    @Test
    void deleteTask_ShouldRemoveTask() throws Exception{
        // Arrange : create and save a task
        Task task = taskRepository.save(new Task("Ancien titre", "Important task", LocalDate.now().plusDays(1), Statut.TODO));
        Long id = task.getId();
        
        // Act
        mockMvc.perform(delete("/api/v1/tasks/{id}", id))
            .andExpect(status().isOk());
            
        // Assert : verify that task doesnt exist
        // assertThat(taskRepository.findById(id), is(Optional.empty())); OU
        assertTrue(taskRepository.findById(id).isEmpty());
    }
}