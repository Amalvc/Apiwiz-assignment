package com.amal.apiwiz.Service.Interface;

import com.amal.apiwiz.Dto.CreateTaskDto;
import com.amal.apiwiz.Dto.TaskPageResponse;
import com.amal.apiwiz.Dto.UpdateTaskDto;
import com.amal.apiwiz.Enum.Status;
import com.amal.apiwiz.Exception.DateException;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Exception.TaskNotFoundRException;
import com.amal.apiwiz.Model.Task;

import java.util.Date;

/**
 * Interface defining task service operations.
 */
public interface TaskService {

    /**
     * Creates a new task.
     *
     * @param createTaskDto- Task object containing details of the task to be created
     * @return - The newly created task
     */
    Task createTask(CreateTaskDto createTaskDto) throws Exception;

    /**
     * Retrieves tasks for a specific user, applying optional filters and pagination.
     *
     * @param userId - ID of the user whose tasks are to be retrieved
     * @param page - Page number for pagination (default: 0)
     * @param size - Number of tasks per page (default: 10)
     * @param sortBy - Field to sort tasks by (default: id)
     * @param sortDirection - Sorting direction (default: asc)
     * @param titleFilter - Filter tasks by title (optional)
     * @param dueDate - Filter tasks by due date (optional)
     * @param filterStatus - Filter tasks by status (optional)
     * @return - TaskPageResponse containing tasks found for the specified user
     */
    TaskPageResponse getTasksByUserId(Long userId, int page, int size, String sortBy, String sortDirection, String titleFilter, Date dueDate, Status filterStatus) throws NotFoundException;

    /**
     * Updates information of an existing task.
     *
     * @param taskId - ID of the task to be updated
     * @param updatedTask - Updated Task object containing new details
     * @return - The updated task
     */
    Task updateTask(Long taskId, UpdateTaskDto updatedTask) throws TaskNotFoundRException;

    /**
     * Deletes a task.
     *
     * @param taskId - ID of the task to be deleted
     */
    void deleteTask(Long taskId) throws TaskNotFoundRException;
}