package com.amal.apiwiz.Service;
import com.amal.apiwiz.Dto.CreateUserDto;
import com.amal.apiwiz.Dto.LoginDto;
import com.amal.apiwiz.Dto.LoginResponseDto;
import com.amal.apiwiz.Dto.SignupResponseDto;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Model.Role;
import com.amal.apiwiz.Model.User;
import com.amal.apiwiz.Repository.RoleRepository;
import com.amal.apiwiz.Repository.UserRepository;
import com.amal.apiwiz.Security.Jwt.JwtUtils;
import com.amal.apiwiz.Service.Implementation.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSignup() throws Exception {
        // Prepare CreateUserDto
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setFirstName("Amal");
        createUserDto.setLastName("vc");
        createUserDto.setEmail("amal@gmail.com");
        createUserDto.setPassword("password123");

        // Mock repository behaviors
        when(userRepository.findByEmail("amal@gmail.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(new Role(1L,"USER"));
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // Call the signup method
        SignupResponseDto response = authService.signup(createUserDto);

        // Verify that values are stored correctly
        verify(userRepository).findByEmail("amal@gmail.com");
        verify(roleRepository).findByName("USER");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
        assertNotNull(response);
    }

    @Test
    void testLogin() throws Exception {
        // Prepare LoginDto
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("amal@gmail.com");
        loginDto.setPassword("password123");

        // Mock authentication manager behavior
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Create a user object
        User user = new User();
        user.setId(1L);
        user.setEmail("amal@gmail.com");
        user.setFirstName("Amal");
        user.setLastName("vc");
        user.setPassword("password123"); // Set password directly without encoding for testing

        // Stub userRepository to return the user
        when(userRepository.findByEmail("amal@gmail.com")).thenReturn(Optional.of(user));

        // Call the login method
        LoginResponseDto response = authService.login(loginDto);

        // Verify that login response is correct
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("amal@gmail.com", response.getEmail());
        assertEquals("Amal", response.getFirstName());
        //assertNotNull(response.getJwt()); // Ensure JWT token is not null

        // Verify that userRepository.findByEmail was called with the correct email
        verify(userRepository).findByEmail("amal@gmail.com");
        // Verify that authenticationManager.authenticate was called with the correct arguments
        //verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        // Verify that SecurityContextHolder.getContext().setAuthentication was called
        //verify(authentication).setAuthenticated(true);
    }

    @Test
    void testAssignUserToAdmin() throws NotFoundException {
        // Mock user and admin role
        User user = new User();
        user.setId(1L);
        user.setEmail("amal@gmail.com");
        user.setFirstName("Amal");
        user.setLastName("vc");
        user.setPassword("password123");

        Role adminRole = new Role(1L,"ADMIN");

        // Stub userRepository to return the user
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        // Stub roleRepository to return the admin role
        when(roleRepository.findByName("ADMIN")).thenReturn(adminRole);

        // Call the method to assign user to admin
        authService.assignUserToAdmin(1L);

        // Verify that userRepository.findById and roleRepository.findByName were called
        verify(userRepository).findById(1L);
        verify(roleRepository).findByName("ADMIN");
        // Verify that user role was updated to admin
        assertEquals(adminRole, user.getRole());
    }
}
