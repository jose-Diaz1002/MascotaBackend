package com.virtualpet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualpet.api.dto.LoginRequest;
import com.virtualpet.api.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 1. Carga el contexto completo de Spring Boot, incluyendo Controllers, Services, etc.
@SpringBootTest
// 2. Le indica a Spring que use el perfil 'test' (carga application-test.properties)
@ActiveProfiles("test")
// 3. Configura MockMvc automáticamente para interactuar con los Controllers
@AutoConfigureMockMvc
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Herramienta para simular peticiones HTTP

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos Java a JSON

    @Test
    void testRegisterAndLoginFlow() throws Exception {
        // --- 1. PREPARACIÓN: Datos de prueba ---
        String testUsername = "testuser";
        String testPassword = "password123";

        RegisterRequest registerRequest = new RegisterRequest(testUsername, testPassword);

        // --- 2. PRUEBA DE REGISTRO (/api/auth/register) ---
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(registerRequest))) // Convierte DTO a JSON
                .andExpect(status().isOk()) // Espera código HTTP 200 OK
                .andExpect(jsonPath("$.token").isNotEmpty()); // Espera que el campo 'token' no esté vacío

        // --- 3. PRUEBA DE LOGIN (/api/auth/login) ---
        LoginRequest loginRequest = new LoginRequest(testUsername, testPassword);

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString(); // Captura la respuesta JSON

        // Opcional: Extraer y devolver el token para usarlo en otras pruebas
        String jwtToken = objectMapper.readTree(loginResponse).get("token").asText();

        // Asumiendo que existe un endpoint de prueba para validar el token (ej. GET /api/pets)
        // Puedes añadir una prueba de acceso a recurso protegido aquí.
    }

    @Test
    void testLoginFailure() throws Exception {
        LoginRequest failedLogin = new LoginRequest("nonexistent", "wrongpassword");

        // El login con credenciales inválidas debe fallar con 401 Unauthorized/Forbidden
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(failedLogin)))
                .andExpect(status().isUnauthorized()); // Espera código 401
    }
}