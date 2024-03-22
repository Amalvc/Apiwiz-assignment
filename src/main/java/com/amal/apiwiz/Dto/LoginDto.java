package com.amal.apiwiz.Dto;

import lombok.Data;

/**
 * Data transfer object (DTO) for login request.
 */
@Data
public class LoginDto {
    private String email;
    private String password;
}