package com.virtualpet.api.service;

import com.virtualpet.api.dto.EquipRequest;
import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.dto.PetResponse;
import com.virtualpet.api.model.Pet;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // --- MÉTODOS DE CONSULTA ---

    public List<PetResponse> getPetsForCurrentUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Pet> pets = petRepository.findByUser(currentUser);
        // Actualizamos las estadísticas de cada mascota ANTES de enviarlas
        pets.forEach(this::updatePetStatsOverTime);
        return pets.stream().map(PetResponse::fromEntity).collect(Collectors.toList());
    }

    public List<PetResponse> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        pets.forEach(this::updatePetStatsOverTime);
        return pets.stream().map(PetResponse::fromEntity).collect(Collectors.toList());
    }

    // --- MÉTODOS DE MODIFICACIÓN ---

    public PetResponse createPet(PetRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet newPet = Pet.builder()
                .name(request.getName())
                .creatureType("Mascota Base")
                .color(request.getColor())
                .hunger(50)
                .happiness(80) // Usamos 'happiness'
                .lastUpdated(LocalDateTime.now()) // Establecemos el tiempo de creación
                .user(currentUser)
                .build();
        return PetResponse.fromEntity(petRepository.save(newPet));
    }

    public void deletePet(Long petId) {
        // (Aquí iría la lógica completa de deletePet que ya tenías)
        petRepository.deleteById(petId);
    }

    // --- MÉTODOS DE INTERACCIÓN ---

    public PetResponse feedPet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet); // Primero, calcula el decaimiento por tiempo
        pet.setHunger(Math.max(0, pet.getHunger() - 30)); // Luego, aplica el efecto de la comida
        pet.setLastUpdated(LocalDateTime.now()); // Finalmente, resetea el tiempo
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse cuddlePet(Long petId) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.min(100, pet.getHappiness() + 20));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse equipAccessory(Long petId, EquipRequest request) {
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!pet.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("No tienes permiso para modificar esta mascota");
        }
        switch (request.getAccessoryType().toLowerCase()) {
            case "hat": pet.setHat(request.getAccessoryName()); break;
            case "hairstyle": pet.setHairstyle(request.getAccessoryName()); break;
            case "shirt": pet.setShirt(request.getAccessoryName()); break;
            case "pants": pet.setPants(request.getAccessoryName()); break;
            case "background": pet.setBackground(request.getAccessoryName()); break;
            default: throw new IllegalArgumentException("Tipo de accesorio no válido: " + request.getAccessoryType());
        }
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    // --- LÓGICA DE SIMULACIÓN DE TIEMPO ---

    private void updatePetStatsOverTime(Pet pet) {
        if (pet.getLastUpdated() == null) {
            pet.setLastUpdated(LocalDateTime.now());
            return;
        }

        long secondsPassed = Duration.between(pet.getLastUpdated(), LocalDateTime.now()).toSeconds();

        if (secondsPassed > 0) {
            // Aumenta el hambre 1 punto cada 30 segundos
            int hungerIncrease = (int) (secondsPassed / 5);
            pet.setHunger(Math.min(100, pet.getHunger() + hungerIncrease));

            // Disminuye la felicidad 1 punto cada 60 segundos
            int happinessDecrease = (int) (secondsPassed / 60);
            pet.setHappiness(Math.max(0, pet.getHappiness() - happinessDecrease));

            // Actualizamos la fecha solo si ha pasado al menos un intervalo
            if (hungerIncrease > 0 || happinessDecrease > 0) {
                pet.setLastUpdated(LocalDateTime.now());
                // No guardamos aquí. El método que llama a esta función se encargará de guardar.
            }
        }
    }
}