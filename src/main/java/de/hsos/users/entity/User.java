package de.hsos.users.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User extends PanacheEntity {
    @NotBlank(message = "Username is mandatory")
    @Size(max = 50, message = "Username must not exceed 50 characters")
    @Column(nullable = false, unique = true)
    public String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false)
    public String password;

    // Default constructor
    public User() {}

    // Constructor with fields
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
