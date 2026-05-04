package com.nck.taxi.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String name;
    private boolean available = true;

    @Enumerated(EnumType.STRING)
    private Role role = Role.DRIVER;  // ← nouveau

    public enum Role {
        DRIVER, ADMIN
    }
}