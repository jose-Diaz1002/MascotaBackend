package com.virtualpet.api.service;

import com.virtualpet.api.dto.AuthResponse;
import com.virtualpet.api.dto.LoginRequest;
import com.virtualpet.api.dto.RegisterRequest;
import com.virtualpet.api.model.Role;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.UserRepository;
import com.virtualpet.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        log.info("Iniciando registro para el usuario: {}", request.getUsername()); // INFO
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER) // Todos los nuevos registros son de tipo USER
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        log.debug("Token JWT generado con éxito para {}", request.getUsername());
        return AuthResponse.builder().token(jwtToken).build();
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Intento de login para el usuario: {}", request.getUsername()); // INFO

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(); // El usuario debería existir si la autenticación fue exitosa
            var jwtToken = jwtService.generateToken(user);
            return AuthResponse.builder().token(jwtToken).build();
        }catch (Exception e) {
            log.error("Fallo de login para {}: {}", request.getUsername(), e.getMessage()); // ERROR
            throw new RuntimeException("Credenciales inválidas");
        }
    }
}
