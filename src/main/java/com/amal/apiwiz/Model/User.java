package com.amal.apiwiz.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

/**
 * Entity class representing a user.
 * Holds information about the user's first name, last name, email, phone number, password,role
 * Implements UserDetails interface for Spring Security authentication.
 */
@Entity
@Table(name = "User_table")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn
    private Role role;

    //One-to-many relationship mapping between User entity and Task entities,Indicates that each User can have multiple Tasks associated with it.
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<Task>tasks=new HashSet<>();

    // Date and time when the user was created
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at",nullable = false,updatable = false)
    private Date createdAt;

    // Date and time when the user was updated
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    /**
     * Retrieves the authorities granted to the user.
     * This method returns a collection of GrantedAuthority objects representing the roles assigned to the user.
     * In this implementation, it retrieves the role of the user and creates a new SimpleGrantedAuthority object with the role name.
     *
     * @return - Collection of GrantedAuthority objects representing the roles assigned to the user
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("grantedAuthority "+ role.getName());
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    /**
     * Retrieves the username used to authenticate the user.
     * In this implementation, it returns the email address of the user.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Checks whether the user's account has expired.
     * In this implementation, it always returns true, indicating that the user's account never expires.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Checks whether the user's account is locked.
     * In this implementation, it always returns true, indicating that the user's account is never locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Checks whether the user's credentials (password) have expired.
     * In this implementation, it always returns true, indicating that the user's credentials never expire.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Checks whether the user is enabled.
     * In this implementation, it always returns true, indicating that the user account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Overrides the equals method to provide custom equality comparison for User objects.
     * Ensures that two User objects are considered equal if they have the same email address.
     * This is useful for comparing User objects based on their unique identifier, which is the email address in this case.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return Objects.equals(email, other.email);
    }

    /**
     * Overrides the hashCode method to generate a custom hash code for User objects.
     * Generates a hash code based on the email field of the User object.
     *
     * @return - The hash code value for the User object
     */
    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}