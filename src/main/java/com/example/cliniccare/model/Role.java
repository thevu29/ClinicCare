package com.example.cliniccare.model;

import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "role_id")
    private UUID roleId;

    private String name;

    private String description;

    @Column(name = "create_at")
    private Date createAt;

    @OneToMany(mappedBy = "role")
    private List<User> userList;

    @PrePersist
    protected void onCreate() {
        createAt = new Date();
    }
}
