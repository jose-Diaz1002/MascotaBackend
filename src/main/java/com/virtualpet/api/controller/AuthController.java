package com.virtualpet.api.controller;

import com.virtualpet.api.dto.AuthResponse;
import com.virtualpet.api.dto.LoginRequest;
import com.virtualpet.api.dto.RegisterRequest;
import com.virtualpet.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación (AUTH)", description = "Registro de nuevos usuarios e inicio de sesión con JWT.")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Registro de Usuario",
            description = "Crea una nueva cuenta de usuario (ROLE_USER) y devuelve un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos necesarios para crear un nuevo usuario (username, password).",
                    required = true
            ),
            responses = @ApiResponse(responseCode = "200", description = "Registro exitoso, token devuelto.")
    )
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Inicio de Sesión",
            description = "Autentica al usuario con credenciales y devuelve un token JWT.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciales para iniciar sesión (username, password).",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login exitoso, token JWT."),
                    @ApiResponse(responseCode = "401", description = "Fallo de autenticación (credenciales incorrectas).")
            }
    )
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

}