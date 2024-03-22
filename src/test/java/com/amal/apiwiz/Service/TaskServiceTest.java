package com.amal.apiwiz.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.amal.apiwiz.Dto.CreateTaskDto;
import com.amal.apiwiz.Dto.TaskPageResponse;
import com.amal.apiwiz.Dto.UpdateTaskDto;
import com.amal.apiwiz.Enum.Status;
import com.amal.apiwiz.Exception.DateException;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Exception.TaskNotFoundRException;
import com.amal.apiwiz.Model.Task;
import com.amal.apiwiz.Model.User;
import com.amal.apiwiz.Repository.TaskRepository;
import com.amal.apiwiz.Repository.UserRepository;
import com.amal.apiwiz.Service.Implementation.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        // You can add any setup needed for your tests here
    }

    @Test
    void testCreateTask_Success() throws Exception {
        // Mock data
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setUserId(1L);
        createTaskDto.setTitle("Test Task");
        createTaskDto.setDescription("Description");

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = formatter.parse("2024-03-25");
        Date dueDate = formatter.parse("2024-03-26");
        createTaskDto.setStartDate(startDate);
        createTaskDto.setDueDate(dueDate);

        User user = new User();
        user.setId(1L);
        Optional<User> optionalUser = Optional.of(user);

        when(userRepository.findById(1L)).thenReturn(optionalUser);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method
        Task createdTask = taskService.createTask(createTaskDto);

        // Assertions
        assertNotNull(createdTask);
        assertEquals(createTaskDto.getTitle(), createdTask.getTitle());
        assertEquals(createTaskDto.getDescription(), createdTask.getDescription());
        assertEquals(createTaskDto.getStartDate(), createdTask.getStartDate());
        assertEquals(createTaskDto.getDueDate(), createdTask.getDueDate());
        assertEquals(Status.PENDING, createdTask.getStatus());
        assertEquals(user, createdTask.getUser());
    }

    @Test
    void testCreateTask_UserNotFound() {
        // Mock data
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Assertions
        assertThrows(NotFoundException.class, () -> taskService.createTask(createTaskDto));
    }
    @Test
    void testGetTasksByUserId_Success() throws NotFoundException {
        // Mock data
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortDirection = "asc";
        String titleFilter = "Test Task";
        Date dueDateFilter = null; // Set to null for this test
        Status statusFilter = Status.PENDING;

        // Mock the behavior of taskRepository
        List<Task> tasks = Collections.singletonList(new Task());
        Page<Task> taskPage = mock(Page.class);
        when(taskPage.getContent()).thenReturn(tasks);
        when(taskPage.getNumber()).thenReturn(page);
        when(taskPage.getSize()).thenReturn(size);
        when(taskPage.getTotalPages()).thenReturn(1);
        when(taskPage.getTotalElements()).thenReturn(1L);
        when(taskRepository.findAll((Specification<Task>) any(), any(Pageable.class))).thenReturn(taskPage);

        // Call the method
        TaskPageResponse result = taskService.getTasksByUserId(userId, page, size, sortBy, sortDirection, titleFilter, dueDateFilter, statusFilter);

        // Assertions
        assertNotNull(result);
        assertEquals(tasks, result.getTasks());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(1, result.getTotalPages());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    void testGetTasksByUserId_NotFound() {
        // Mock data
        Long userId = 1L;
        int page = 0;
        int size = 10;
        String sortBy = "id";
        String sortDirection = "asc";
        String titleFilter = "Non-existent Task";
        Date dueDateFilter = null; // Set to null for this test
        Status statusFilter = Status.PENDING;

        // Mock the behavior of taskRepository to return an empty page
        Page<Task> taskPage = mock(Page.class);
        when(taskPage.isEmpty()).thenReturn(true);
        when(taskRepository.findAll((Specification<Task>) any(), any(Pageable.class))).thenReturn(taskPage);

        // Assertions
        assertThrows(NotFoundException.class, () ->
                taskService.getTasksByUserId(userId, page, size, sortBy, sortDirection, titleFilter, dueDateFilter, statusFilter)
        );
    }

    @Test
    void testUpdateTask_Success() throws TaskNotFoundRException {
        // Mock data
        Long taskId = 1L;
        UpdateTaskDto updatedTaskDto = new UpdateTaskDto();
        updatedTaskDto.setTitle("Updated Title");
        updatedTaskDto.setDescription("Updated Description");
        updatedTaskDto.setDueDate(new Date());
        updatedTaskDto.setStatus(Status.COMPLETED);

        Task existingTask = new Task();
        existingTask.setId(taskId);

        // Stub the behavior of the task repository to return the existing task when findById is called
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        // Stub the behavior of the task repository to return the saved task when save is called
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        // Call the updateTask method
        Task updatedTask = taskService.updateTask(taskId, updatedTaskDto);

        // Verify that taskRepository.findById was called with the correct taskId
        verify(taskRepository).findById(taskId);
        // Verify that the task is saved with the updated values
        assertEquals(updatedTaskDto.getTitle(), updatedTask.getTitle());
        assertEquals(updatedTaskDto.getDescription(), updatedTask.getDescription());
        assertEquals(updatedTaskDto.getDueDate(), updatedTask.getDueDate());
        assertEquals(updatedTaskDto.getStatus(), updatedTask.getStatus());
    }

    @Test
    void testDeleteTask_Success() throws TaskNotFoundRException {
        // Mock data
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);

        // Stub the behavior of the task repository to return the existing task when findById is called
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Call the deleteTask method
        taskService.deleteTask(taskId);

        // Verify that taskRepository.findById was called with the correct taskId
        verify(taskRepository).findById(taskId);
        // Verify that taskRepository.delete was called with the correct task
        verify(taskRepository).delete(existingTask);
    }
}


