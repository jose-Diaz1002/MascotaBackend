package com.virtualpet.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtualpet.api.dto.LoginRequest;
import com.virtualpet.api.model.Role;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String ADMIN_USERNAME = "admintest";
    private static final String ADMIN_PASSWORD = "adminpass";
    private String adminToken;

    @BeforeEach
    void setup() throws Exception {
        if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
            User admin = User.builder()
                    .username(ADMIN_USERNAME)
                    .password(passwordEncoder.encode(ADMIN_PASSWORD))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }

        LoginRequest loginRequest = new LoginRequest(ADMIN_USERNAME, ADMIN_PASSWORD);
        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();

        this.adminToken = objectMapper.readTree(loginResponse).get("token").asText();
    }

    @Test
    void testAccessAdminEndpointWithAdminRole_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/admin/pets")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void testAccessAdminEndpointWithoutToken_ShouldFail401() throws Exception {
        mockMvc.perform(get("/api/admin/pets")
                        .contentType("application/json"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessAdminEndpointWithUserRole_ShouldFail403() throws Exception {
        String userToken = registerAndGetToken("normaluser", "userpass", Role.ROLE_USER);

        mockMvc.perform(get("/api/admin/pets")
                        .header("Authorization", "Bearer " + userToken)
                        .contentType("application/json"))
                .andExpect(status().isForbidden());
    }

    private String registerAndGetToken(String username, String password, Role role) throws Exception {
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
