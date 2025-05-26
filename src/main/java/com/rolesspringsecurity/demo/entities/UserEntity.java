package com.rolesspringsecurity.demo.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Email
    @NotBlank
    @Size(max = 30)
    private String username;

    @Email
    @NotBlank
    @Size(max = 50)
    private String email;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RolesEntity.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "table_user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RolesEntity> rolesUser;

}
