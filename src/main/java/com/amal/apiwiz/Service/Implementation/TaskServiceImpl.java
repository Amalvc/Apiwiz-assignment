package com.amal.apiwiz.Service.Implementation;

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
import com.amal.apiwiz.Service.Interface.TaskService;
import com.amal.apiwiz.Service.TaskSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.Optional;

/**
 * Implementation of the TaskService interface.
 */
@Service
public class TaskServiceImpl implements TaskService {

    //Injecting dependencies
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Creates a new task based on the provided task details.
     *
     * @param createTaskDto - Task object containing details of the task to be created
     * @return - The newly created task
     * @throws NotFoundException - If the user associated with the task is not found
     */
    @Override
    public Task createTask(CreateTaskDto createTaskDto) throws Exception {
        // Retrieve the user associated with the task,If not found then throws NotFoundException
        Optional<User> user=userRepository.findById(createTaskDto.getUserId());
        if(user.isEmpty()){
            throw new NotFoundException();
        }

        // Start date must less than end date , can't create tasks for date less than current
        Date currentDate = new Date();
        if (createTaskDto.getStartDate().before(currentDate)||createTaskDto.getDueDate().before(createTaskDto.getStartDate())) {
            throw new DateException();
        }

        // Build the task object using the provided task details
        Task task=Task.builder().
                title(createTaskDto.getTitle()).
                description(createTaskDto.getDescription()).
                startDate(createTaskDto.getStartDate()).
                dueDate(createTaskDto.getDueDate()).
                status(Status.PENDING).
                user(user.get()).build();

        // Save the task in the database
        Task details =taskRepository.save(task);
        return details;
    }

    /**
     * Retrieves tasks associated with a particular user, applying optional filters and pagination.
     *
     * @param userId - ID of the user whose tasks are to be retrieved
     * @return - TaskPageResponse containing tasks found for the specified user
     * @throws NotFoundException - If no tasks are found for the specified user
     */
    @Override
    public TaskPageResponse getTasksByUserId(Long userId, int page, int size, String sortBy, String sortDirection, String titleFilter, Date dueDateFilter, Status statusFilter) throws NotFoundException {
        // Create Pageable object for pagination and sorting
        Pageable pageable = sortDirection.equalsIgnoreCase("asc") ?
                PageRequest.of(page, size, Sort.by(sortBy).ascending()) :
                PageRequest.of(page, size, Sort.by(sortBy).descending());

        // Build Specification based on provided filters
        Specification<Task> spec = Specification.where(TaskSpecifications.withUserId(userId));

        // Check if title filter is provided and not empty
        if (titleFilter != null && !titleFilter.isEmpty()) {
            // If title filter is provided and not empty, add a specification to filter tasks by title
            spec = spec.and(TaskSpecifications.titleContains(titleFilter));
        }

        // Check if due date filter is provided
        if (dueDateFilter != null) {
            // If due date filter is provided, add a specification to filter tasks by due date
            spec = spec.and(TaskSpecifications.dueDateEquals(dueDateFilter));
        }

        // Check if status filter is provided
        if (statusFilter != null) {
            // If status filter is provided, add a specification to filter tasks by status
            spec = spec.and(TaskSpecifications.statusEquals(statusFilter));
        }

        // Retrieve tasks matching the specification and pagination
        Page<Task> pageResult = taskRepository.findAll(spec, pageable);
        // Check if tasks are found
        if (pageResult.isEmpty()) {
            // Throw NotFoundException if no tasks are found
            throw new NotFoundException();
        }

        // Construct TaskPageResponse containing the retrieved tasks
        return new TaskPageResponse(pageResult.getContent(), pageResult.getNumber(), pageResult.getSize(), pageResult.getTotalPages(), pageResult.getTotalElements());
    }

    /**
     * Updates information of an existing task.
     *
     * @param taskId - ID of the task to be updated
     * @param updatedTask - Updated Task object containing new details
     * @return - The updated task
     * @throws TaskNotFoundRException - If the specified task is not found
     */
    @Override
    public Task updateTask(Long taskId, UpdateTaskDto updatedTask) throws TaskNotFoundRException {
        // Retrieve the task by ID,If it is not found then throws TaskNotFoundException
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundRException());

        // Update task attributes based on non-null fields in the DTO
        updateTaskAttributes(task, updatedTask);

        // Save and return the updated task
        return taskRepository.save(task);
    }

    /**
     * Updates the attributes of a task with values from the provided UpdateTaskDto.
     * If the corresponding fields in the UpdateTaskDto are not null, the task's attributes are updated.
     *
     * @param task         The task to be updated.
     * @param updatedTask  The DTO containing updated task attributes.
     */
    private void updateTaskAttributes(Task task, UpdateTaskDto updatedTask) {
        // Update task attributes if the corresponding fields in the DTO are not null
        if (updatedTask.getTitle() != null) {
            task.setTitle(updatedTask.getTitle());
        }
        if (updatedTask.getDescription() != null) {
            task.setDescription(updatedTask.getDescription()); // Update task description
        }
        if (updatedTask.getDueDate() != null) {
            task.setDueDate(updatedTask.getDueDate()); // Update task due date
        }
        if (updatedTask.getStatus() != null) {
            task.setStatus(updatedTask.getStatus()); // Update task status
        }
    }

    /**
     * Deletes a task.
     *
     * @param taskId - ID of the task to be deleted
     * @throws TaskNotFoundRException - If the specified task is not found
     */
    @Override
    public void deleteTask(Long taskId) throws TaskNotFoundRException {
        // Retrieve the task by ID
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundRException());

        // Delete the task from the database
        taskRepository.delete(task);
    }
}
