package com.amal.apiwiz.Controller;

import com.amal.apiwiz.Dto.*;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Service.Implementation.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related APIs.
 * Maps endpoints for user signup, login, and role assignment.
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    // Injecting auth service
    @Autowired
    private AuthServiceImpl authService;

    /**
     * Handles POST requests to "/api/auth/signup" endpoint.
     * Creates a new user account based on provided details.
     * Returns a response entity indicating success or failure of the operation.
     *
     * @param createUserDto - DTO containing user details for signup
     * @return - ResponseEntity with success or failure message
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody CreateUserDto createUserDto)throws Exception{
        log.info("Register new user.");

        SignupResponseDto response=authService.signup(createUserDto);
        CommonResponseDto result=new CommonResponseDto(true,201,"Successfully created new account",response);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * Handles POST requests to "/api/auth/login" endpoint.
     * Authenticates user based on provided email and password.
     * Returns a response entity containing login response with JWT token.
     *
     * @param loginDto - DTO containing login credentials
     * @return - ResponseEntity containing login response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto)throws Exception{
        log.info("Received login request.");

        LoginResponseDto response=authService.login(loginDto);
        CommonResponseDto result=new CommonResponseDto(true,200,"Login Success",response);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     * Handles PUT requests to "/api/auth/assign-admin/{userId}" endpoint.
     * Assigns the ADMIN role to the user identified by userId.
     *
     * @param userId - ID of the user to assign the ADMIN role
     * @return - ResponseEntity indicating success of the role assignment
     */
    @PutMapping("/assign-admin/{userId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<?> assignUserToAdmin(@PathVariable Long userId) throws NotFoundException {
        log.info("Received request to change role from USER to ADMIN.");

        authService.assignUserToAdmin(userId);
        CommonResponseDto result=new CommonResponseDto(true,200,"Role changed to ADMIN",null);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
