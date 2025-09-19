// src/main/java/com/virtualpet/api/repository/PetRepository.java
package com.virtualpet.api.repository;

import com.virtualpet.api.model.Pet;
import com.virtualpet.api.model.User; // Importa User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Importa List

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // Spring Data JPA creará la consulta automáticamente
    List<Pet> findByUser(User user);
}