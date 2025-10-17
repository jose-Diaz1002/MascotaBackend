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
import org.springframework.cache.annotation.CacheEvict; // <-- Nuevo
import org.springframework.cache.annotation.Cacheable; // <-- Nuevo
import lombok.extern.slf4j.Slf4j; // <-- (Opcional, p

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// @Service: Marca esta clase como un servicio de Spring (lógica de negocio)
// @RequiredArgsConstructor: Lombok genera un constructor con los campos final
@Service
@RequiredArgsConstructor
@Slf4j
public class PetService {

    // Repositorio para acceder a la base de datos de mascotas
    private final PetRepository petRepository;

    /**
     * Obtiene todas las mascotas del usuario actual (ROLE_USER)
     * 1. Obtiene el usuario autenticado del contexto de seguridad
     * 2. Busca todas las mascotas de ese usuario
     * 3. Actualiza las estadísticas basándose en el tiempo transcurrido
     * 4. Convierte las entidades Pet a PetResponse (DTO)
     */

    // El nombre del almacén de caché es 'pets'
    @Cacheable(value = "pets", key = "#currentUser.id")
    public List<PetResponse> getPetsForCurrentUser() {
        log.info("Buscando mascotas para el usuario desde la DB (no desde caché)"); // Se ve solo la primera vez
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Pet> pets = petRepository.findByUser(currentUser);
        pets.forEach(this::updatePetStatsOverTime); // Actualiza hambre y felicidad según tiempo
        return pets.stream().map(PetResponse::fromEntity).collect(Collectors.toList());
    }

    /**
     * Obtiene TODAS las mascotas del sistema (ROLE_ADMIN)
     * Similar a getPetsForCurrentUser pero sin filtrar por usuario
     */
    public List<PetResponse> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        pets.forEach(this::updatePetStatsOverTime);
        return pets.stream().map(PetResponse::fromEntity).collect(Collectors.toList());
    }

    /**
     * Crea una nueva mascota para el usuario actual
     * Valores iniciales:
     * - hunger: 50 (medianamente hambrienta)
     * - happiness: 80 (bastante feliz)
     * - lastUpdated: ahora (para calcular decaimiento futuro)
     */

    // Al crear una nueva mascota, se debe invalidar la caché de la lista de mascotas del usuario
    @CacheEvict(value = "pets", key = "#result.userId") // El key debe ser el ID del usuario afectado
    public PetResponse createPet(PetRequest request) {
        log.info("Creando nueva mascota para el usuario.");
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Pet newPet = Pet.builder()
                .name(request.getName())
                .creatureType("Mascota Base")
                .color("#FFA500") // Color naranja por defecto
                .hunger(50)
                .happiness(80)
                .lastUpdated(LocalDateTime.now())
                .user(currentUser)
                .build();
        return PetResponse.fromEntity(petRepository.save(newPet));
    }

    /**
     * Elimina una mascota por su ID
     */
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

    /**
     * Alimenta a la mascota
     * 1. Actualiza estadísticas por tiempo transcurrido
     * 2. Reduce el hambre en 30 puntos (mínimo 0)
     * 3. Resetea lastUpdated para futuras actualizaciones
     */


    public PetResponse feedPet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet); // Primero calcula el decaimiento
        pet.setHunger(Math.max(0, pet.getHunger() - 30)); // Reduce hambre
        pet.setLastUpdated(LocalDateTime.now()); // Resetea el tiempo
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    /**
     * Acaricia/mima a la mascota
     * Aumenta la felicidad en 20 puntos (máximo 100)
     */
    public PetResponse cuddlePet(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.min(100, pet.getHappiness() + 20));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    /**
     * Equipa accesorios a la mascota (sombrero, peinado, camisa, pantalones, fondo)
     * Verifica que el usuario actual sea el dueño de la mascota
     */
    public PetResponse equipAccessory(Long petId, EquipRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Verificación de seguridad: solo el dueño puede modificar
        if (!pet.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("No tienes permiso para modificar esta mascota");
        }

        // Aplica el accesorio según el tipo
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

    /**
     * MÉTODO CLAVE: Actualiza las estadísticas basándose en el tiempo transcurrido
     *
     * Este método calcula cuánto tiempo ha pasado desde la última actualización
     * y ajusta el hambre y la felicidad en consecuencia:
     *
     * - HAMBRE: Aumenta 1 punto cada 5 segundos
     * - FELICIDAD: Disminuye 1 punto cada 60 segundos (1 minuto)
     *
     * Esto significa que el backend NO guarda los cambios graduales del frontend,
     * sino que recalcula todo basándose en el tiempo transcurrido.
     *
     */
    private void updatePetStatsOverTime(Pet pet) {
        // Si no hay lastUpdated, lo inicializa
        if (pet.getLastUpdated() == null) {
            pet.setLastUpdated(LocalDateTime.now());
            return;
        }

        // Calcula cuántos segundos han pasado desde la última actualización
        long secondsPassed = Duration.between(pet.getLastUpdated(), LocalDateTime.now()).toSeconds();

        if (secondsPassed > 0) {
            // HAMBRE: Aumenta 1 punto cada 5 segundos
            // Ejemplo: Si pasaron 15 segundos, hambre aumenta 3 puntos
            int hungerIncrease = (int) (secondsPassed / 5);
            pet.setHunger(Math.min(100, pet.getHunger() + hungerIncrease));

            // FELICIDAD: Disminuye 1 punto cada 60 segundos
            // Ejemplo: Si pasaron 120 segundos, felicidad disminuye 2 puntos
            int happinessDecrease = (int) (secondsPassed / 60);
            pet.setHappiness(Math.max(0, pet.getHappiness() - happinessDecrease));

            // Solo actualiza lastUpdated si hubo cambios
            if (hungerIncrease > 0 || happinessDecrease > 0) {
                pet.setLastUpdated(LocalDateTime.now());
            }
        }
    }

    // ========== MÉTODOS ADICIONALES (añadidos por v0) ==========
    // Estos métodos permiten ajustar manualmente las estadísticas

    /**
     * Disminuye la felicidad en una cantidad específica
     */
    public PetResponse decreaseHappiness(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.max(0, pet.getHappiness() - amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    /**
     * Aumenta la felicidad en una cantidad específica
     */

    // Al modificar una mascota, invalidamos la entrada de caché de ese usuario
    @CacheEvict(value = "pets", key = "@userService.getCurrentUser().id") // Asume un método para obtener el ID del usuario
    public PetResponse increaseHappiness(Long petId, int amount) {
        log.info("Aumentando felicidad de la mascota ID {}", petId);
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHappiness(Math.min(100, pet.getHappiness() + amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    /**
     * Aumenta el hambre en una cantidad específica
     */
    public PetResponse increaseHunger(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHunger(Math.min(100, pet.getHunger() + amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }

    /**
     * Disminuye el hambre en una cantidad específica
     */
    public PetResponse decreaseHunger(Long petId, int amount) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        updatePetStatsOverTime(pet);
        pet.setHunger(Math.max(0, pet.getHunger() - amount));
        pet.setLastUpdated(LocalDateTime.now());
        return PetResponse.fromEntity(petRepository.save(pet));
    }
}