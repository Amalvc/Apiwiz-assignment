package com.amal.apiwiz.Security.UserSecurity;
import com.amal.apiwiz.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service class for loading user details by username (email) for authentication purposes.
 */
@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    /**
     * Loads user details by username (email).
     *
     * @param email - Email of the user
     * @return - UserDetails object representing the user
     * @throws UsernameNotFoundException - Thrown if the user with the provided email is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Retrieves the user details from the database using the provided email
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
