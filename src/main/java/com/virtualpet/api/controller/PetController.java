package com.virtualpet.api.controller;

import com.virtualpet.api.dto.EquipRequest;
import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.dto.PetResponse;
import com.virtualpet.api.model.User;
import com.virtualpet.api.service.PetService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@Tag(name = "Gestión de Mascotas", description = "CRUD y manipulación de las mascotas virtuales del usuario.")
public class PetController {

    private final PetService petService;

    @GetMapping
    public List<PetResponse> getPets(Principal principal) {
        User currentUser = petService.loadUserByUsername(principal.getName());
        return petService.getPetsForCurrentUser(currentUser);
    }

    @PostMapping
    public PetResponse createPet(@RequestBody PetRequest request, Principal principal) {
        User currentUser = petService.loadUserByUsername(principal.getName());
        return petService.createPet(request, currentUser);
    }

    @PutMapping("/{id}")
<<<<<<< HEAD
    @Operation(summary = "Actualizar estadísticas de la mascota", description = "Permite actualizar los valores de hambre y felicidad.")
    public ResponseEntity<PetResponse> updatePetStats(
            @PathVariable Long id,
            @RequestBody PetRequest request) {
        return ResponseEntity.ok(petService.updatePetStats(id, request));
    }

=======
    @Operation(
            summary = "Actualizar estadísticas de la mascota",
            description = "Permite actualizar hambre y felicidad de una mascota concreta."
    )
    public ResponseEntity<PetResponse> updatePet(
            @PathVariable Long id,
            @RequestBody PetRequest request,
            Principal principal) {

        User currentUser = petService.loadUserByUsername(principal.getName());
        PetResponse updatedPet = petService.updatePet(id, request, currentUser);
        return ResponseEntity.ok(updatedPet);
    }


>>>>>>> ee2a42679cf523c86b2fe5cbc034ea39a1dcd313
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/feed")
    public ResponseEntity<PetResponse> feedPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.feedPet(id));
    }

    @PostMapping("/{id}/cuddle")
    public ResponseEntity<PetResponse> cuddlePet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.cuddlePet(id));
    }

    @PostMapping("/{id}/equip")
    public ResponseEntity<PetResponse> equipAccessory(@PathVariable Long id, @RequestBody EquipRequest request) {
        return ResponseEntity.ok(petService.equipAccessory(id, request));
    }

    @PostMapping("/{id}/increase-happiness")
    public ResponseEntity<PetResponse> increaseHappiness(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount) {
        return ResponseEntity.ok(petService.increaseHappiness(id, amount));
    }

    @PostMapping("/{id}/decrease-happiness")
    public ResponseEntity<PetResponse> decreaseHappiness(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount) {
        return ResponseEntity.ok(petService.decreaseHappiness(id, amount));
    }

    @PostMapping("/{id}/increase-hunger")
    public ResponseEntity<PetResponse> increaseHunger(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount) {
        return ResponseEntity.ok(petService.increaseHunger(id, amount));
    }

    @PostMapping("/{id}/decrease-hunger")
    public ResponseEntity<PetResponse> decreaseHunger(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount) {
        return ResponseEntity.ok(petService.decreaseHunger(id, amount));
    }
}
