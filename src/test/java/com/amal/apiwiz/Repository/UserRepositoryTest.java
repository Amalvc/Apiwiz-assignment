package com.amal.apiwiz.Repository;

import com.amal.apiwiz.Model.Role;
import com.amal.apiwiz.Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    User user;

    @BeforeEach
    void setup() {
        // Create a Role
        Role role = new Role();
        role.setName("USER");
        roleRepository.save(role);

        user = new User();
        user.setFirstName("Amal");
        user.setLastName("vc");
        user.setEmail("amal@gmail.com");
        user.setPassword("password123");
        user.setRole(role);

        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testFindByEmail() {
        // Find user by email
        Optional<User> foundUserOptional = userRepository.findByEmail("amal@gmail.com");

        // Check if user is present
        assertThat(foundUserOptional).isPresent();

        // Get the found user
        User foundUser = foundUserOptional.get();

        // Check user details
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(foundUser.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(foundUser.getLastName()).isEqualTo(user.getLastName());
    }
}
