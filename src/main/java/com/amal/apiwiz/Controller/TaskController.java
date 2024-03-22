package com.amal.apiwiz.Controller;

import com.amal.apiwiz.Dto.CommonResponseDto;
import com.amal.apiwiz.Dto.CreateTaskDto;
import com.amal.apiwiz.Dto.TaskPageResponse;
import com.amal.apiwiz.Dto.UpdateTaskDto;
import com.amal.apiwiz.Enum.Status;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Exception.TaskNotFoundRException;
import com.amal.apiwiz.Model.Task;
import com.amal.apiwiz.Service.Implementation.TaskServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Controller for managing tasks.
 * Maps endpoints for task creation, retrieval, updating, and deletion.
 */
@RestController
@RequestMapping("/api/tasks")
@Slf4j
public class TaskController {

    @Autowired
    private TaskServiceImpl taskService;

    /**
     * Handles POST requests to "/api/tasks/create" endpoint.
     * Creates a new task.
     * Admin and Super_Admin is only allowed to access this API
     *
     * @param createTaskDto - DTO containing task details for creation
     * @return - ResponseEntity with the created task details
     * @throws NotFoundException if the specified user is not found
     */
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> createTask(@RequestBody CreateTaskDto createTaskDto) throws Exception {
        log.info("Create new task.");

        Task createdTask = taskService.createTask(createTaskDto);
        CommonResponseDto response = new CommonResponseDto(true, 201, "Task created successfully", createdTask);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Handles GET requests to "/api/tasks/user/{userId}" endpoint.
     * Retrieve tasks for a specific user.
     * If the logged-in account role is USER, then ID-based validation will be performed.
     * If the logged-in account role is ADMIN or SUPER_ADMIN, then there is no ID-based validation.
     *
     * @param userId - ID of the user whose tasks are to be retrieved
     * @param page - Page number for pagination (default: 0)
     * @param size - Number of tasks per page (default: 10)
     * @param sortBy - Field to sort tasks by (default: id)
     * @param sortDirection - Sorting direction (default: asc)
     * @param titleFilter - Filter tasks by title (optional)
     * @param dueDateFilter - Filter tasks by due date (optional)
     * @param statusFilter - Filter tasks by status (optional)
     * @return - ResponseEntity containing tasks found for the specified user
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')or hasAuthority('USER') and principal.id==#userId")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(required = false) String titleFilter,
            @RequestParam(required = false) Date dueDateFilter,
            @RequestParam(required = false) Status statusFilter
    ) throws NotFoundException {
        log.info("Fetch task assigned to a user.");

        TaskPageResponse tasks = taskService.getTasksByUserId(userId, page, size, sortBy,sortDirection, titleFilter,dueDateFilter,statusFilter);
        CommonResponseDto response = new CommonResponseDto(true, 200, "Tasks found for user with ID: " + userId, tasks);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


    /**
     * Handles PUT requests to "/api/tasks/update/{taskId}" endpoint.
     * Updates an existing task.
     * Admin and Super_Admin is only allowed to access this API
     *
     * @param taskId - ID of the task to be updated
     * @param updatedTask - Updated task details
     * @return - ResponseEntity indicating success or failure of the task update
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PutMapping("/update/{taskId}")
    public ResponseEntity<CommonResponseDto> updateTask(@PathVariable Long taskId, @RequestBody UpdateTaskDto updatedTask)throws TaskNotFoundRException {
        log.info("Update task details mainly status of task");

        Task task = taskService.updateTask(taskId, updatedTask);
        CommonResponseDto response = new CommonResponseDto(true, 200, "Task updated successfully", task);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    /**
     * Handles DELETE requests to "/api/tasks/delete/{taskId}" endpoint.
     * Deletes an existing task.
     * Admin and Super_Admin is only allowed to access this API
     *
     * @param taskId - ID of the task to be deleted
     * @return - ResponseEntity indicating success of the task deletion
     */
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<CommonResponseDto> deleteTask(@PathVariable Long taskId)throws TaskNotFoundRException {
        log.info("Delete task");

        taskService.deleteTask(taskId);
        CommonResponseDto response = new CommonResponseDto(true, 200, "Task deleted successfully", null);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}