package com.amal.apiwiz.Exception;

import com.amal.apiwiz.Dto.CommonResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * It provides centralized exception handling for various exceptions that may occur during
 * the execution of controller methods.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles the exception when a user already exists in the system.
     */
    @ExceptionHandler({UserAlreadyExistsException.class})
    public ResponseEntity handleEmailAlreadyRegisteredException() {
        log.error("Email already registered");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 409, "Email address provided is already registered with an account", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.CONFLICT);
        return responseEntity;
    }
    /**
     * Handles the exception when a user not found exception.
     */
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handleNotFoundException() {
        log.error("Email already registered");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 404, "User not found", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    /**
     * Handles the exception when a task not found exception.
     */
    @ExceptionHandler({TaskNotFoundRException.class})
    public ResponseEntity handleTaskNotFoundException() {
        log.error("Email already registered");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 404, "Task not found", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.NOT_FOUND);
        return responseEntity;
    }

    /**
     * Handles the exception when invalid credentials are provided during authentication.
     */
    @ExceptionHandler({InvalidCredentialException.class})
    public ResponseEntity handleInvalidCredentialException() {
        log.error("Email or password is wrong");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 401, "Email or password is wrong", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.UNAUTHORIZED);
        return responseEntity;
    }


    /**
     * Handles the exception when access is unauthorized.
     */
    @ExceptionHandler({UnauthorizedException.class})
    public ResponseEntity handleUnauthorizedException() {
        log.error("Forbidden no access ");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 403, "Forbidden no access", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.FORBIDDEN);
        return responseEntity;
    }

    /**
     * Handles the exception when start date is greater than end date or current date is greater than start date of task.
     */
    @ExceptionHandler({DateException.class})
    public ResponseEntity handleDateException() {
        log.error("start date is greater than end date or current date is greater than start date of task");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 400, "start date is greater than end date or current date is greater than start date of task", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.BAD_REQUEST);
        return responseEntity;
    }

    /**
     * Handles the exception when jwt token is invalid or expired.
     */
    @ExceptionHandler({InvalidJwtTokenException.class})
    public ResponseEntity handleInvalidJwtException() {
        log.error("Invalid JWT token");

        CommonResponseDto responseDTO = new CommonResponseDto(false, 401, "Jwt token is invalid or expired", null);
        ResponseEntity responseEntity = new ResponseEntity(responseDTO, HttpStatus.UNAUTHORIZED);
        return responseEntity;
    }
    /**
     * Handles the exception when a runtime exception occurs.
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity handleRuntimeException(RuntimeException ex) {
        log.error("Runtime Exception occurred: {}", ex.getMessage(), ex);

        CommonResponseDto responseDTO = new CommonResponseDto(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null);
        return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}