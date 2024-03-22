package com.amal.apiwiz.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity class representing a role.
 */
@Entity
@Table(name = "Role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    //name ->ADMIN,SUPER_ADMIN,USER three types of role
    @Column(nullable = false)
    private String name;
}
