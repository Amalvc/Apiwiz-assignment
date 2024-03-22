package com.amal.apiwiz.Service.Implementation;

import com.amal.apiwiz.Convertor.UserConvertor;
import com.amal.apiwiz.Dto.CreateUserDto;
import com.amal.apiwiz.Dto.LoginDto;
import com.amal.apiwiz.Dto.LoginResponseDto;
import com.amal.apiwiz.Dto.SignupResponseDto;
import com.amal.apiwiz.Exception.InvalidCredentialException;
import com.amal.apiwiz.Exception.NotFoundException;
import com.amal.apiwiz.Exception.UserAlreadyExistsException;
import com.amal.apiwiz.Model.Role;
import com.amal.apiwiz.Model.User;
import com.amal.apiwiz.Repository.RoleRepository;
import com.amal.apiwiz.Repository.UserRepository;
import com.amal.apiwiz.Security.Jwt.JwtUtils;
import com.amal.apiwiz.Service.Interface.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;


/**
 * Service class responsible for authentication-related operations.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    //Injecting dependencies with the help of @Autowired
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    /**
     * Registers a new user in the system.
     * Checks if the user with the provided email already exists in the database.
     * If the email is unique, creates a new user and saves it along with the associated address.
     *
     * @param userDto - DTO containing user details for registration
     * @throws UserAlreadyExistsException - Thrown if a user with the provided email already exists
     */
    @Override
    public SignupResponseDto signup(CreateUserDto userDto)throws Exception {
        // Checks if a user with the provided email already exists in the database then it throw exception
        Optional<User> sameEmail= userRepository.findByEmail(userDto.getEmail());
        if(sameEmail.isPresent()){
            throw new UserAlreadyExistsException();
        }

        //fetching role 'USER' from db which is already added in database
        Role role=roleRepository.findByName("USER");

        // Constructs a new user object from the userDto
        User user=User.builder().firstName(userDto.getFirstName()).
                lastName(userDto.getLastName()).
                email(userDto.getEmail()).
                password(passwordEncoder.encode(userDto.getPassword())).
                role(role).
                build();

        userRepository.save(user);

        // This constructs a SignupResponseDto object based on the provided User object.
        SignupResponseDto response= UserConvertor.SignupResponseBuilder(user);
        return response;

    }

    /**
     * Authenticates a user based on the provided login credentials.
     * Generates a JWT token upon successful authentication.
     *
     * @param loginDto - DTO containing login credentials
     * @return - LoginResponse containing user details and JWT token
     * @throws IllegalArgumentException - Thrown if the provided email is invalid
     */
    @Override
    public LoginResponseDto login(LoginDto loginDto)throws Exception{
        Authentication authentication = null;

        //validate user credential with authenticationManager
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        } catch (Exception e) {
            throw new InvalidCredentialException(); // Throw exception if authentication fails
        }

        //fetch user using email
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialException());

        // Sets the authenticated user in the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generates a JWT token for the authenticated user
        String jwt = jwtUtils.generateJwtTokenForUser(user);

        // Constructs a new LoginResponse  with user details and JWT token
        LoginResponseDto response = LoginResponseDto.builder().
                id(user.getId()).email(user.getEmail()).
                firstName(user.getFirstName()).lastName(user.getLastName()).
                jwt(jwt).build();

        return response; // Returns the LoginResponse
    }

    /**
     * Assigns the role of ADMIN to the specified user.
     *
     * @param userId - ID of the user to be assigned the ADMIN role
     * @throws NotFoundException - If the user with the specified ID is not found or if the ADMIN role is not found
     */
    public void assignUserToAdmin(Long userId) throws NotFoundException {
        // Retrieve the user by ID,If user is not found then throws NotFoundException
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException();
        }

        User user = userOptional.get();

        // Retrieve admin role from db
        Role adminRole = roleRepository.findByName("ADMIN");
        if (adminRole == null) {
            throw new NotFoundException();
        }

        // Assign admin role to the user
        user.setRole(adminRole);
        userRepository.save(user);
    }
}