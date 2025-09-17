package com.virtualpet.api.repository;

import com.virtualpet.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA generará automáticamente la consulta SQL para este método
    // basándose en el nombre del método. Lo usaremos para cargar el usuario
    // durante el proceso de autenticación.
    Optional<User> findByUsername(String username);
}
