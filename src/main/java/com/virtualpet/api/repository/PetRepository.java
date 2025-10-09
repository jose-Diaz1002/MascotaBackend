package com.virtualpet.api.repository;

import com.virtualpet.api.model.Pet;
import com.virtualpet.api.model.User; // Importa User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; // Importa List

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByUser(User user);
}