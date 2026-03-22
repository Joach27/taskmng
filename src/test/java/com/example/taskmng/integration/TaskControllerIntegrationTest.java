package com.example.taskmng.integration;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;   
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.taskmng.model.Task;
import com.example.taskmng.repository.TaskRepository;
// import com.example.taskmng.service.TaskService;

@SpringBootTest
@AutoConfigureMockMvc                   // Pour tester les endpoints HTTP
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Simuler les requêtes HTTP
    
    @Autowired 
    private TaskRepository taskRepository;
    
    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        taskRepository.save(new Task());
        taskRepository.save(new Task());
    }
    
    // @Autowired
    // private TaskService taskService;
    
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
        mockMvc.perform(get("/api/v1/task/{id}", savedTask.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.titre").value("Default title"));
    }
}