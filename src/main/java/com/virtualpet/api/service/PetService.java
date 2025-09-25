package com.virtualpet.api.service;

import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.dto.PetResponse; // Importante: Usamos el DTO
import com.virtualpet.api.model.Pet;
import com.virtualpet.api.model.Role;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // --- MÉTODOS DE CONSULTA ---

    public List<PetResponse> getPetsForCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return petRepository.findByUser(currentUser)
                .stream()
                .map(PetResponse::fromEntity) // Convertimos cada Pet a PetResponse
                .collect(Collectors.toList());
    }

    public List<PetResponse> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(PetResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // --- MÉTODOS DE MODIFICACIÓN ---

    public PetResponse createPet(PetRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet newPet = Pet.builder()
                .name(request.getName())
                .creatureType("Humo")
                .color(request.getColor())
                .specialFeatures(request.getSpecialFeatures())
                .hunger(50)
                .sadness(20)
                .user(currentUser)
                .build();

        Pet savedPet = petRepository.save(newPet);
        return PetResponse.fromEntity(savedPet); // Devolvemos el DTO
    }

    public PetResponse updatePet(Long petId, PetRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet petToUpdate = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!petToUpdate.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new SecurityException("No tienes permiso para actualizar esta mascota");
        }

        petToUpdate.setName(request.getName());
        petToUpdate.setColor(request.getColor());
        petToUpdate.setSpecialFeatures(request.getSpecialFeatures());

        Pet updatedPet = petRepository.save(petToUpdate);
        return PetResponse.fromEntity(updatedPet); // Devolvemos el DTO
    }

    public void deletePet(Long petId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet petToDelete = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!petToDelete.getUser().getId().equals(currentUser.getId()) && !currentUser.getRole().equals(Role.ROLE_ADMIN)) {
            throw new SecurityException("No tienes permiso para eliminar esta mascota");
        }

        petRepository.delete(petToDelete);
    }

    // --- MÉTODOS DE INTERACCIÓN ---

    public PetResponse feedPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        pet.setHunger(Math.max(0, pet.getHunger() - 25));
        Pet savedPet = petRepository.save(pet);
        return PetResponse.fromEntity(savedPet); // Devolvemos el DTO
    }

    public PetResponse cuddlePet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        pet.setSadness(Math.max(0, pet.getSadness() - 20));
        Pet savedPet = petRepository.save(pet);
        return PetResponse.fromEntity(savedPet); // Devolvemos el DTO
    }
}