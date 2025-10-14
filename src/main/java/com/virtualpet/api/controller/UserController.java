package com.virtualpet.api.controller;

import com.virtualpet.api.dto.RoleChangeRequest;
import com.virtualpet.api.dto.UserResponse;
import com.virtualpet.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/users - Obtiene todos los usuarios (solo ADMIN)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * PUT /api/users/{userId}/role - Cambia el rol de un usuario (solo ADMIN)
     */
    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable Long userId,
            @RequestBody RoleChangeRequest request) {
        UserResponse updatedUser = userService.changeUserRole(userId, request.getNewRole());
        return ResponseEntity.ok(updatedUser);
    }
}