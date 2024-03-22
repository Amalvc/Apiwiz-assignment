package com.amal.apiwiz.Service.Interface;

import com.amal.apiwiz.Dto.CreateUserDto;
import com.amal.apiwiz.Dto.LoginDto;
import com.amal.apiwiz.Dto.LoginResponseDto;
import com.amal.apiwiz.Dto.SignupResponseDto;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Exception.RoleNotFoundException;

/**
 * Interface defining authentication service operations.
 */
public interface AuthService {

    /**
     * Registers a new user.
     *
     * @param createUserDto - DTO containing user details for registration
     * @return - SignupResponseDto containing the response details upon successful registration
     */
    SignupResponseDto signup(CreateUserDto createUserDto) throws Exception;

    /**
     * Authenticates a user based on provided login credentials.
     *
     * @param loginDto - DTO containing login credentials
     * @return - LoginResponseDto containing user details and JWT token upon successful authentication
     */
    LoginResponseDto login(LoginDto loginDto)throws Exception;

    /**
     * Assigns a user to an administrator role.
     *
     * @param userId - ID of the user to be assigned as an administrator
     * @throws NotFoundException - If the specified user is not found
     * @throws RoleNotFoundException - If role is not present in db
     */
    void assignUserToAdmin(Long userId) throws NotFoundException, RoleNotFoundException;
}