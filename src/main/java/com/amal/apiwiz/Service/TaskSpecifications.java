package com.amal.apiwiz.Service;

import com.amal.apiwiz.Enum.Status;
import com.amal.apiwiz.Model.Task;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

/**
 * Utility class for creating specifications for Task entities.
 */
public class TaskSpecifications {

    /**
     * Creates a specification to filter tasks by user ID.
     *
     * @param userId - User ID to filter tasks by
     * @return - Specification to filter tasks by user ID
     */
    public static Specification<Task> withUserId(Long userId) {
        return (root, query, builder) -> builder.equal(root.get("user").get("id"), userId);
    }

    /**
     * Creates a specification to filter tasks by title containing the given text.
     *
     * @param titleFilter - Text to filter tasks by title
     * @return - Specification to filter tasks by title containing the given text
     */
    public static Specification<Task> titleContains(String titleFilter) {
        return (root, query, builder) -> builder.like(root.get("title"), "%" + titleFilter + "%");
    }

    /**
     * Creates a specification to filter tasks by due date matching the given date.
     *
     * @param dueDateFilter - Date to filter tasks by due date
     * @return - Specification to filter tasks by due date matching the given date
     */
    public static Specification<Task> dueDateEquals(Date dueDateFilter) {
        //return (root, query, builder) -> builder.equal(root.get("dueDate"), dueDateFilter);
        return (root, query, builder) -> {
            // Get the path to the dueDate attribute
            Path<Date> dueDatePath = root.get("dueDate");
            // Create a condition to match the dueDate with the given date
            return builder.and(
                    // Use the "date" function to extract the date part of the dueDate
                    builder.equal(builder.function("date", Date.class, dueDatePath), dueDateFilter)
            );
        };
    }

    /**
     * Creates a specification to filter tasks by status matching the given status.
     *
     * @param statusFilter - Status to filter tasks by
     * @return - Specification to filter tasks by status matching the given status
     */
    public static Specification<Task> statusEquals(Status statusFilter) {
        return (root, query, builder) -> builder.equal(root.get("status"), statusFilter);
    }
}
