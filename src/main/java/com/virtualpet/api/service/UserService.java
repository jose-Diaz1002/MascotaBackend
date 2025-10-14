package com.virtualpet.api.service;

import com.virtualpet.api.dto.UserResponse;
import com.virtualpet.api.model.Role;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Obtiene todos los usuarios del sistema (solo para ADMIN)
     */
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .role(user.getRole().name()) // Convierte el enum a String
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Cambia el rol de un usuario
     */
    public UserResponse changeUserRole(Long userId, String newRole) {
        // Buscar el usuario
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        // Validar que el rol sea válido
        if (!newRole.equals("ROLE_USER") && !newRole.equals("ROLE_ADMIN")) {
            throw new IllegalArgumentException("Rol inválido. Use ROLE_USER o ROLE_ADMIN");
        }

        // Cambiar el rol
        user.setRole(Role.valueOf(newRole));
        User updatedUser = userRepository.save(user);

        // Devolver la respuesta
        return UserResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .role(updatedUser.getRole().name())
                .build();
    }
}