package com.amal.apiwiz.Dto;
import com.amal.apiwiz.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Data transfer object (DTO) for creating a task.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTaskDto {
    private String title;
    private String description;
    private Date startDate;
    private Date dueDate;
    private Long userId; // UserId to fetch User from DB
}