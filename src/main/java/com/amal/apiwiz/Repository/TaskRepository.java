package com.amal.apiwiz.Repository;

import com.amal.apiwiz.Model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * Task repository for managing CRUD operation
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    /**
     * Finds all tasks matching the given specification and paginates the results.
     *
     * @param spec - Specification to filter tasks
     * @param pageable - Pageable object specifying pagination parameters
     * @return - Page containing tasks that match the given specification
     */
    Page<Task> findAll(Specification<Task> spec, Pageable pageable);
}
