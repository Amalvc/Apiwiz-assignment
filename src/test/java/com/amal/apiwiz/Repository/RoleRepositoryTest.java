package com.amal.apiwiz.Repository;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.amal.apiwiz.Model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindRoleByName() {
        // Create a role object
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        // Save the role object
        roleRepository.save(role);

        // Retrieve the role by name
        Optional<Role> foundRoleOptional = Optional.ofNullable(roleRepository.findByName("ADMIN"));

        // Assert that the role is present
        assertThat(foundRoleOptional).isPresent();

        // Get the found role
        Role foundRole = foundRoleOptional.get();

        // Assert that the found role has the correct attributes
        assertThat(foundRole.getName()).isEqualTo("ADMIN");
    }
}

