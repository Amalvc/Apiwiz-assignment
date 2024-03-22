package com.amal.apiwiz.Dto;
import com.amal.apiwiz.Enum.Status;
import jakarta.annotation.Nullable;
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
public class UpdateTaskDto {
    @Nullable
    private String title;

    @Nullable
    private String description;

    @Nullable
    private Date dueDate;

    @Nullable
    private Status status;

    @Nullable
    private Long userId;
}