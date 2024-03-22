package com.amal.apiwiz.Model;

import com.amal.apiwiz.Enum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.*;

/**
 * Entity class representing a task.
 * Holds information about the task title,description,dueDate,status
 */
@Entity
@Table(name = "Task")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false)
    private Status status;

    //Many-to-one relationship mapping between Task entity and User entities,Indicates that each User can have multiple Tasks associated with it.
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User user;

}
