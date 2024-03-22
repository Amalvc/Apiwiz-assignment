package com.amal.apiwiz.Controller;

import com.amal.apiwiz.Dto.CreateTaskDto;
import com.amal.apiwiz.Dto.TaskPageResponse;
import com.amal.apiwiz.Dto.UpdateTaskDto;
import com.amal.apiwiz.Model.Task;
import com.amal.apiwiz.Security.Jwt.JwtUtils;
import com.amal.apiwiz.Security.UserSecurity.UserDetailService;
import com.amal.apiwiz.Service.Implementation.TaskServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskServiceImpl taskService;

    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserDetailService userDetailService;

    @Test
    @WithMockUser(username = "admin", roles = {"ROLE","SUPER_ADMIN"})
    public void testCreateTask() throws Exception {
        CreateTaskDto createTaskDto = new CreateTaskDto();

        Task createdTask = new Task();
        createdTask.setId(1L);
        createdTask.setTitle("Sample Task");

        when(taskService.createTask(any(CreateTaskDto.class))).thenReturn(createdTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createTaskDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Task created successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ROLE","SUPER_ADMIN"})
    public void testGetTasksByUserId() throws Exception {
        Long userId = 1L; // Sample user ID

        TaskPageResponse taskPageResponse = new TaskPageResponse(); // Create a sample task page response

        when(taskService.getTasksByUserId(anyLong(), anyInt(), anyInt(), anyString(), anyString(), anyString(), any(), any())).thenReturn(taskPageResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/user/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Tasks found for user with ID: " + userId));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"SUPER_ADMIN"})
    public void testUpdateTask() throws Exception {
        Long taskId = 1L; // Sample task ID
        UpdateTaskDto updateTaskDto = new UpdateTaskDto();

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Task");

        when(taskService.updateTask(anyLong(), any(UpdateTaskDto.class))).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/update/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateTaskDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Task updated successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"SUPER_ADMIN"})
    public void testDeleteTask() throws Exception {
        Long taskId = 1L;

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/delete/{taskId}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Task deleted successfully"));
    }

    // Utility method to convert object to JSON string
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
