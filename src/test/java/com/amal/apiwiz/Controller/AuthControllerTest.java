package com.amal.apiwiz.Controller;

import com.amal.apiwiz.Dto.CreateUserDto;
import com.amal.apiwiz.Dto.LoginDto;
import com.amal.apiwiz.Dto.LoginResponseDto;
import com.amal.apiwiz.Dto.SignupResponseDto;
import com.amal.apiwiz.Security.Jwt.JwtUtils;
import com.amal.apiwiz.Security.UserSecurity.UserDetailService;
import com.amal.apiwiz.Service.Implementation.AuthServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthServiceImpl authService;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private UserDetailService userDetailService;

    // Utility method to convert object to JSON string
    private String asJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithMockUser()
    void signUp_ValidUser_ReturnsCreatedResponse() throws Exception {
        // Prepare mock data
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setFirstName("Amal");
        createUserDto.setLastName("vc");
        createUserDto.setEmail("amal@gmail.com");
        createUserDto.setPassword("password123");

        SignupResponseDto signupResponseDto = new SignupResponseDto();
        signupResponseDto.setId(1L);
        signupResponseDto.setFirstName("Amal");
        signupResponseDto.setLastName("vc");
        signupResponseDto.setEmail("amal@gmail.com");
        signupResponseDto.setRole("USER");

        // Mock the behavior of authService.signup method
        when(authService.signup(any(CreateUserDto.class))).thenReturn(signupResponseDto);

        // Perform the POST request
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createUserDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Successfully created new account"));
    }

    @Test
    @WithMockUser(username = "amal@gmail.com", password = "password123")
    void testLogin() throws Exception {
        // Prepare mock data
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("amal@gmail.com");
        loginDto.setPassword("password123");

        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setId(1L);
        loginResponseDto.setFirstName("Amal");
        loginResponseDto.setLastName("vc");
        loginResponseDto.setEmail("amal@gmail.com");
        loginResponseDto.setJwt("sample_jwt_token");

        // Mock the behavior of authService.login method
        when(authService.login(any(LoginDto.class))).thenReturn(loginResponseDto);

        // Perform the POST request
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loginDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Login Success"))
                .andExpect(jsonPath("$.data.jwt").isString());

    }

    @Test
    @WithMockUser(username = "superadmin@example.com", roles = {"SUPER_ADMIN"})
    void assignUserToAdmin_Success() throws Exception {
        // Prepare mock data
        Long userId = 1L;

        // Perform the PUT request
        mockMvc.perform(put("/api/auth/assign-admin/{userId}", userId).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("Role changed to ADMIN"));
    }

    @Test
    @WithMockUser(username = "regularuser@example.com", roles = {"USER"})
    void assignUserToAdmin_Unauthorized() throws Exception {
        // Prepare mock data
        Long userId = 1L;

        // Perform the PUT request
        mockMvc.perform(put("/api/auth/assign-admin/{userId}", userId))
                .andExpect(status().isForbidden()); // Expecting 403 Forbidden as the regular user is not authorized
    }
}

