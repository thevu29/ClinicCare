package com.example.cliniccare.service;

import com.example.cliniccare.dto.RoleDTO;
import com.example.cliniccare.exception.ResourceNotFoundException;
import com.example.cliniccare.model.Role;
import com.example.cliniccare.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleDTO> getRoles() {
        try {
            List<Role> roles = roleRepository.findAll();
            return roles.stream().map(RoleDTO::new).toList();
        } catch (Exception e) {
            logger.error("Failed to get roles: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get roles", e);
        }
    }

    public RoleDTO getRoleById(UUID id) {
        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            return new RoleDTO(role);
        } catch (Exception e) {
            logger.error("Failed to get user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get user", e);
        }
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        try {
            Role role = new Role();
            role.setName(roleDTO.getName());
            role.setDescription(roleDTO.getDescription());

            Role savedRole = roleRepository.save(role);
            return new RoleDTO(savedRole);
        } catch (Exception e) {
            logger.error("Failed to create role: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create role", e);
        }
    }
}
