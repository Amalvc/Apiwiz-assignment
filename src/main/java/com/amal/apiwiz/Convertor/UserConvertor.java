package com.amal.apiwiz.Convertor;

import com.amal.apiwiz.Dto.SignupResponseDto;
import com.amal.apiwiz.Model.User;

/**
 * Utility class for converting User entities to DTOs.
 */
public class UserConvertor {
    /**
     * Builds a SignupResponseDto from a User entity.
     *
     * @param user - User entity to convert
     * @return - SignupResponseDto containing user details
     */
    public static SignupResponseDto SignupResponseBuilder(User user){
        // Build SignupResponseDto from User entity
        SignupResponseDto response=SignupResponseDto.builder().
                id(user.getId()).
                firstName(user.getFirstName()).
                lastName(user.getLastName()).
                email(user.getEmail()).
                role(user.getRole().getName()).build();
        return response;
    }
}
