package com.virtualpet.api.controller;

import com.virtualpet.api.dto.RoleChangeRequest;
import com.virtualpet.api.dto.UserResponse;
import com.virtualpet.api.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Gestión de Usuarios", description = "Endpoints para administradores para listar y modificar usuarios.")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar Todos los Usuarios",
            description = "Devuelve una lista de todos los usuarios registrados. Requiere rol ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios devuelta."),
                    @ApiResponse(responseCode = "403", description = "Prohibido: No tiene rol ADMIN.")
            }
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Cambiar Rol de Usuario",
            description = "Permite a un administrador cambiar el rol de un usuario por su ID.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Nuevo rol a asignar (ROLE_USER o ROLE_ADMIN).",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Rol actualizado con éxito."),
                    @ApiResponse(responseCode = "403", description = "Prohibido: No tiene rol ADMIN."),
                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado.")
            }
    )
    public ResponseEntity<UserResponse> changeUserRole(
            @Parameter(description = "ID del usuario cuyo rol será modificado.")
            @PathVariable Long userId,
            @RequestBody RoleChangeRequest request) {
        UserResponse updatedUser = userService.changeUserRole(userId, request.getNewRole());
        return ResponseEntity.ok(updatedUser);
    }
}