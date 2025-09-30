package com.example.banking.controller;

import com.example.banking.entity.Role;
import com.example.banking.dto.ApiResponse;
import com.example.banking.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Fetch all roles.
     * Only accessible by ADMIN users.
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Role>>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();

            ApiResponse<List<Role>> response = ApiResponse.<List<Role>>builder()
                    .status(HttpStatus.OK.value())
                    .message("Roles fetched successfully")
                    .data(roles)
                    .build();

            return ResponseEntity.ok(response);

        } catch (Exception ex) {
            ApiResponse<List<Role>> errorResponse = ApiResponse.<List<Role>>builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Failed to fetch roles: " + ex.getMessage())
                    .data(null)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
