package com.amal.apiwiz.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) for signup response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignupResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
