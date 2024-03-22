package com.amal.apiwiz.Dto;

import com.amal.apiwiz.Model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data transfer object (DTO) for task response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskPageResponse {
    private List<Task> tasks;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
}