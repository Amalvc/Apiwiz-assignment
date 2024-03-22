package com.amal.apiwiz.Controller;

import com.amal.apiwiz.Dto.CreateTaskDto;
import com.amal.apiwiz.Dto.TaskPageResponse;
import com.amal.apiwiz.Dto.UpdateTaskDto;
import com.amal.apiwiz.Enum.Status;
import com.amal.apiwiz.Model.Task;
import com.amal.apiwiz.Service.Implementation.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskServiceImpl taskService;

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void createTask_Success() throws Exception {
        // Prepare mock data for request
        CreateTaskDto createTaskDto = new CreateTaskDto();
        // Set task details in createTaskDto

        // Mock service method behavior
        when(taskService.createTask(any(CreateTaskDto.class))).thenReturn(new Task());

        // Perform POST request
        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createTaskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task created successfully"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void getTasksByUserId_Success() throws Exception {
        // Prepare mock data
        Long userId = 1L;
        // Set other parameters as needed

        // Mock service method behavior
        when(taskService.getTasksByUserId(anyLong(), anyInt(), anyInt(), anyString(), anyString(), anyString(), any(Date.class), any(Status.class)))
                .thenReturn(new TaskPageResponse());

        // Perform GET request
        mockMvc.perform(get("/api/tasks/user/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Tasks found for user with ID: " + userId));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void updateTask_Success() throws Exception {
        // Prepare mock data for request
        Long taskId = 1L;
        Task updatedTask = new Task();
        // Set updated task details

        // Mock service method behavior
        when(taskService.updateTask(anyLong(), any(UpdateTaskDto.class))).thenReturn(new Task());

        // Perform PUT request
        mockMvc.perform(put("/api/tasks/update/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task updated successfully"));
    }

    @Test
    @WithMockUser(roles = "SUPER_ADMIN")
    void deleteTask_Success() throws Exception {
        // Prepare mock data
        Long taskId = 1L;

        // Mock service method behavior
        doNothing().when(taskService).deleteTask(anyLong());

        // Perform DELETE request
        mockMvc.perform(delete("/api/tasks/delete/{taskId}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Task deleted successfully"));
    }

    // Utility method to convert object to JSON string
    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

