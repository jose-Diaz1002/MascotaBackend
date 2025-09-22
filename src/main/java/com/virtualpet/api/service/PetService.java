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
    // --- Método de Creación Actualizado ---
    public Pet createPet(PetRequest petRequest) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet newPet = Pet.builder()
                .name(petRequest.getName())
                .creatureType("Humo") // Siempre será "Humo"
                .color(petRequest.getColor())
                .specialFeatures(petRequest.getSpecialFeatures())
                .user(currentUser)
                .hunger(50)  // Estado inicial
                .sadness(20) // Estado inicial
                .build();
        return petRepository.save(newPet);
    }
    // --- NUEVOS MÉTODOS DE INTERACCIÓN ---
    public Pet feedPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        // La comida reduce el hambre. Math.max asegura que no baje de 0.
        pet.setHunger(Math.max(0, pet.getHunger() - 25));
        return petRepository.save(pet);
    }

    public Pet cuddlePet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        // Mimar reduce la tristeza.
        pet.setSadness(Math.max(0, pet.getSadness() - 20));
        return petRepository.save(pet);
    }

    // Método para obtener las mascotas del usuario autenticado
    public List<Pet> getPetsForCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Esto requiere añadir un método al repositorio
        return petRepository.findByUser(currentUser);
    }
    // Añade estos métodos dentro de la clase PetService.java

    // En PetService.java

    public Pet updatePet(Long petId, PetRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet petToUpdate = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        // Verificación de seguridad (esta parte está correcta)
        if (!petToUpdate.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new SecurityException("No tienes permiso para actualizar esta mascota");
        }

        // --- CORRECCIÓN AQUÍ ---
        // Actualizamos solo los campos que el usuario puede cambiar
        petToUpdate.setName(request.getName());
        petToUpdate.setColor(request.getColor());
        petToUpdate.setSpecialFeatures(request.getSpecialFeatures());

        // La línea que daba error se ha eliminado.

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