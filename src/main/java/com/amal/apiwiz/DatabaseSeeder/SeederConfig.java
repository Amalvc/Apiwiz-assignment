package com.amal.apiwiz.DatabaseSeeder;

import com.amal.apiwiz.Model.Role;
import com.amal.apiwiz.Model.User;
import com.amal.apiwiz.Repository.RoleRepository;
import com.amal.apiwiz.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


/**
 * Configuration class responsible for seeding initial data into the database.
 */
@Configuration
public class SeederConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Command line runner bean to seed roles (ADMIN, SUPER_ADMIN, USER) into the database.
     *
     * @param roleRepository The repository for Role entities.
     * @return The CommandLineRunner instance.
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public CommandLineRunner roleSeeder(RoleRepository roleRepository) {
        return args -> {
            // Logic to seed roles (ADMIN, SUPER_ADMIN, USER)
            if (roleRepository.count()==0) {
                // Logic to seed roles (ADMIN, SUPER_ADMIN, USER)
                Role adminRole = new Role();
                adminRole.setName("ADMIN");
                Role superAdminRole = new Role();
                superAdminRole.setName("SUPER_ADMIN");
                Role userRole = new Role();
                userRole.setName("USER");

                roleRepository.saveAll(List.of(adminRole, superAdminRole, userRole));
            }
        };
    }

    /**
     * Command line runner bean to seed super admin credentials into the database.
     *
     * @param userRepository The repository for User entities.
     * @param roleRepository The repository for Role entities.
     * @return The CommandLineRunner instance.
     */
    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    public CommandLineRunner superAdminCredentialSeeder(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.count()==0) {
                // Logic to seed super admin credentials
                Role superAdminRole = roleRepository.findByName("SUPER_ADMIN");

                User superAdmin = new User();
                superAdmin.setFirstName("Super");
                superAdmin.setLastName("Admin");
                superAdmin.setEmail("superadmin@gmail.com");
                superAdmin.setPassword(passwordEncoder.encode("password123"));
                superAdmin.setRole(superAdminRole);

                userRepository.save(superAdmin);
            }
        };
    }
}
