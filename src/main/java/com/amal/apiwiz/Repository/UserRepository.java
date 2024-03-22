package com.amal.apiwiz.Repository;

import com.amal.apiwiz.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * User repository for managing CRUD operation
 */
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    /**
     * Finds a user by email.
     *
     * @param email - Email of the user
     * @return - Optional containing the user with the specified email, if found
     */
    Optional<User> findByEmail(String email);
}
