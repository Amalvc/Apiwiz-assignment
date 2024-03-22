package com.amal.apiwiz.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import com.amal.apiwiz.Enum.Status;
import com.amal.apiwiz.Model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testFindAllTasksWithSpecificationAndPagination() {
        // Create a task object
        Task task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Description of Task 1");
        task.setDueDate(new Date());
        task.setStatus(Status.IN_PROGRESS);

        // Save the task object
        taskRepository.save(task);

        // Create a specification to filter tasks
        Specification<Task> spec = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("title"), "Task 1");

        // Create a pageable object for pagination
        Pageable pageable = PageRequest.of(0, 10);

        // Retrieve tasks using the specification and pagination
        Page<Task> tasksPage = taskRepository.findAll(spec, pageable);

        // Assert that the tasks page is not null
        assertThat(tasksPage).isNotNull();

        // Assert that the tasks page contains the saved task
        assertThat(tasksPage.getContent()).isNotEmpty();
        assertThat(tasksPage.getContent().get(0).getTitle()).isEqualTo("Task 1");
    }
}

