package com.amal.apiwiz.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) for common response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponseDto {
    private boolean status;
    private int code;
    private String message;
    private Object data;
}