package com.example.cliniccare.controller;

import com.example.cliniccare.dto.RoleDTO;
import com.example.cliniccare.exception.ResourceNotFoundException;
import com.example.cliniccare.response.ApiResponse;
import com.example.cliniccare.service.RoleService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("api/role")
public class RoleController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getRoles() {
        try {
            List<RoleDTO> roles = roleService.getRoles();
            return ResponseEntity.ok(new ApiResponse<>(
                    true, "Get roles successfully", roles
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(
                    false, e.getMessage(), null
            ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable UUID id) {
        try {
            RoleDTO role = roleService.getRoleById(id);
            return ResponseEntity.ok(new ApiResponse<>(
                    true, "Get role successfully", role
            ));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(
                    false, e.getMessage(), null
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new ApiResponse<>(
                    false, e.getMessage(), null
            ));
        }
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDTO roleDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errors = bindingResult.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest().body(new ApiResponse<>(
                        false, errors, null
                ));
            }

            RoleDTO role = roleService.createRole(roleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(
                    true, "Create role successfully", role
            ));
        } catch (Exception e) {
            logger.error("Failed to create role: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(
                    false, "Failed to create role", null
            ));
        }
    }
}
