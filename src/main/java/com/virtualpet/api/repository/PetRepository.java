package com.virtualpet.api.repository;

import com.virtualpet.api.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // Por ahora no necesitamos métodos personalizados aquí.
    // JpaRepository ya nos da todo lo que necesitamos para el CRUD básico.
}
