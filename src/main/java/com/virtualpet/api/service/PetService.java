package com.virtualpet.api.service;

import com.virtualpet.api.dto.EquipRequest;
import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.dto.PetResponse;
import com.virtualpet.api.model.Pet;
import com.virtualpet.api.model.User;
import com.virtualpet.api.repository.PetRepository;
import com.virtualpet.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;

    @Cacheable(value = "pets", key = "#user.id")
    public List<PetResponse> getPetsForCurrentUser(User user) {
        List<Pet> pets = petRepository.findByUser(user);
        pets.forEach(this::updatePetStatsOverTime);
        return pets.stream().map(PetResponse::fromEntity).collect(Collectors.toList());
    }

    public List<PetResponse> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        pets.forEach(this::updatePetStatsOverTime);
        return pets.stream().map(PetResponse::fromEntity).collect(Collectors.toList());
    }

    @CacheEvict(value = "pets", key = "#user.id")
    public PetResponse createPet(PetRequest request, User user) {
        log.info("Creando nueva mascota para el usuario.");
        Pet newPet = Pet.builder()
                .name(request.getName())
                .creatureType("Mascota Base")
                .color("#FFA500")
                .hunger(80)
                .happiness(50)
                .lastUpdated(LocalDateTime.now())
                .user(user)
                .build();
        return PetResponse.fromEntity(petRepository.save(newPet));
    }

    public PetResponse updatePet(Long id, PetRequest request, User user) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        if (!pet.getUser().getId().equals(user.getId())) {
            throw new SecurityException("No tienes permiso para modificar esta mascota");
        }

        pet.setHappiness(request.getHappiness());
        pet.setHunger(request.getHunger());
        pet.setLastUpdated(LocalDateTime.now());
        petRepository.save(pet);

        return PetResponse.fromEntity(pet);
    }


    public User loadUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    public PetResponse feedPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHunger(Math.max(0, pet.getHunger() - 10)); // estaba enn 30
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse cuddlePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.min(100, pet.getHappiness() + 20));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse equipAccessory(Long petId, EquipRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!pet.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("No tienes permiso para modificar esta mascota");
        }

        switch (request.getAccessoryType().toLowerCase()) {
            case "hat":
                pet.setHat(request.getAccessoryName());
                break;
            case "hairstyle":
                pet.setHairstyle(request.getAccessoryName());
                break;
            case "shirt":
                pet.setShirt(request.getAccessoryName());
                break;
            case "pants":
                pet.setPants(request.getAccessoryName());
                break;
            case "background":
                pet.setBackground(request.getAccessoryName());
                break;
            default:
                throw new IllegalArgumentException("Tipo de accesorio no válido: " + request.getAccessoryType());
        }
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    private void updatePetStatsOverTime(Pet pet) {
        if (pet.getLastUpdated() == null) {
            pet.setLastUpdated(LocalDateTime.now());
            return;
        }
        long secondsPassed = Duration.between(pet.getLastUpdated(), LocalDateTime.now()).toSeconds();

        if (secondsPassed > 0) {
            int hungerIncrease = (int) (secondsPassed / 30);
            pet.setHunger(Math.min(100, pet.getHunger() + hungerIncrease));

            int happinessDecrease = (int) (secondsPassed / 30);
            pet.setHappiness(Math.max(0, pet.getHappiness() - happinessDecrease));

            if (hungerIncrease > 0 || happinessDecrease > 0) {
                pet.setLastUpdated(LocalDateTime.now());
            }
        }
    }

    public PetResponse decreaseHappiness(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.max(0, pet.getHappiness() - amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    @CacheEvict(value = "pets", key = "@userService.getCurrentUser().id")
    public PetResponse increaseHappiness(Long petId, int amount) {
        log.info("Aumentando felicidad de la mascota ID {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.min(100, pet.getHappiness() + amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse increaseHunger(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHunger(Math.min(100, pet.getHunger() + amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    public PetResponse decreaseHunger(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHunger(Math.max(0, pet.getHunger() - amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }
}