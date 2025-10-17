package com.virtualpet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualpet.api.dto.LoginRequest;
import com.virtualpet.api.model.Role;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// ... (otros imports)
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class AdminIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admintest";
    private static final String ADMIN_PASSWORD = "adminpass";
    private String adminToken;

    @BeforeEach // Se ejecuta antes de cada test para asegurar datos y token
    void setup() throws Exception {
        // 1. Limpiar y crear usuario ADMIN en la DB de prueba
        if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
            User admin = User.builder()
                    .username(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }

        // 2. Obtener el token ADMIN
        LoginRequest loginRequest = new LoginRequest(ADMIN_USERNAME, ADMIN_PASSWORD);
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        this.adminToken = objectMapper.readTree(loginResponse).get("token").asText();
    }

    @Test
    void testAccessAdminEndpointWithAdminRole_ShouldSucceed() throws Exception {
        // Prueba el acceso al endpoint GET /api/admin/pets (protegido por ADMIN)
        mockMvc.perform(get("/api/admin/pets")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType("application/json"))
                .andExpect(status().isOk()); // Espera 200 OK
    }

    @Test
    void testAccessAdminEndpointWithoutToken_ShouldFail401() throws Exception {
        // Acceso sin token
        mockMvc.perform(get("/api/admin/pets")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized()); // Espera 401 UNAUTHORIZED
    }

    @Test
    void testAccessAdminEndpointWithUserRole_ShouldFail403() throws Exception {
        // 1. Crear un usuario normal (ROLE_USER)
        String userToken = registerAndGetToken("normaluser", "userpass", Role.ROLE_USER);

        // 2. Intentar acceder al endpoint ADMIN con el token USER
        mockMvc.perform(get("/api/admin/pets")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType("application/json"))
                .andExpect(status().isForbidden()); // Espera 403 FORBIDDEN
    }

    // Método auxiliar para registrar y obtener token de otros usuarios
    private String registerAndGetToken(String username, String password, Role role) throws Exception {
        // ... Lógica de registro o creación directa en DB para obtener el token...
        // Por simplicidad, se puede implementar usando mockMvc.perform(post("/api/auth/register")...
        // o creando el usuario directamente en el userRepository si es necesario.

        // Aquí se usará la lógica de login después de asegurar que el usuario existe
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role(role)
                    .build();
            userRepository.save(user);
        }

        LoginRequest loginRequest = new LoginRequest(username, password);
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readTree(loginResponse).get("token").asText();
    }
}
