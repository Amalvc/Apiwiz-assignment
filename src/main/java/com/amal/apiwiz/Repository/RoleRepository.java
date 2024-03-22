package com.amal.apiwiz.Repository;

import com.amal.apiwiz.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Role entities.
 * Provides methods for CRUD operations and custom queries on Role entities.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    /**
     * Finds a role by its name.
     *
     * @param name - Name of the role to find
     * @return - Role entity corresponding to the given name
     */
    Role findByName(String name);

}
