package com.virtualpet.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data // Lombok: genera getters, setters, toString, etc.
@Builder // Lombok: para construir objetos de forma fluida
@NoArgsConstructor // Lombok: genera un constructor sin argumentos
@AllArgsConstructor // Lombok: genera un constructor con todos los argumentos
@Entity // JPA: marca esta clase como una entidad de base de datos
@Table(name = "users") // JPA: especifica el nombre de la tabla
public class User implements UserDetails {

    @Id // JPA: marca este campo como la clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA: la BD genera el ID automáticamente
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "El nombre de usuario debe tener entre 3 y 20 caracteres")
    @Column(unique = true, nullable = false) // JPA: el username debe ser único y no nulo
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // JPA: guarda el enum como un String ("ROLE_USER") en la BD
    @Column(nullable = false)
    private Role role;

    // Métodos de UserDetails que Spring Security utilizará
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
