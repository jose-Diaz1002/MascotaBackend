// src/main/java/com/virtualpet/api/service/PetService.java
package com.virtualpet.api.service;

import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.model.Pet;
import com.virtualpet.api.model.Role;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // Método para crear una nueva mascota
    public Pet createPet(PetRequest petRequest) {
        // Obtenemos el usuario que está haciendo la petición
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Pet newPet = Pet.builder()
                .name(petRequest.getName())
                .creatureType(petRequest.getCreatureType())
                .color(petRequest.getColor())
                .user(currentUser) // Asignamos la mascota al usuario actual
                .mood("Feliz") // Valores por defecto
                .energyLevel(100)
                .build();

        return petRepository.save(newPet);
    }

    // Método para obtener las mascotas del usuario autenticado
    public List<Pet> getPetsForCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Esto requiere añadir un método al repositorio
        return petRepository.findByUser(currentUser);
    }
    // Añade estos métodos dentro de la clase PetService.java

    public Pet updatePet(Long petId, PetRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet petToUpdate = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        // Verificación de seguridad: el usuario debe ser el dueño o un admin
        if (!petToUpdate.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new SecurityException("No tienes permiso para actualizar esta mascota");
        }

        petToUpdate.setName(request.getName());
        petToUpdate.setCreatureType(request.getCreatureType());
        petToUpdate.setColor(request.getColor());

        return petRepository.save(petToUpdate);
    }

    public void deletePet(Long petId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet petToDelete = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        // Verificación de seguridad: el usuario debe ser el dueño o un admin
        if (!petToDelete.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new SecurityException("No tienes permiso para eliminar esta mascota");
        }

        petRepository.delete(petToDelete);
    }
}