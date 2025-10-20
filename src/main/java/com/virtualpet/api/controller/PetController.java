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
    @Operation(
            summary = "Obtener Mascotas del Usuario",
            description = "Devuelve una lista de todas las mascotas del usuario autenticado. Resultado cacheado.",
            responses = @ApiResponse(responseCode = "200", description = "Lista de mascotas (PetResponse).")
    )
    public List<PetResponse> getPets(Principal principal) {
        User currentUser = petService.loadUserByUsername(principal.getName());
        return petService.getPetsForCurrentUser(currentUser);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PetResponse>> getAllPetsForAdmin() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @PostMapping
    @Operation(
            summary = "Crear Nueva Mascota",
            description = "Registra una nueva mascota virtual para el usuario autenticado.",
            responses = @ApiResponse(responseCode = "200", description = "Mascota creada exitosamente.")
    )
    public PetResponse createPet(@RequestBody PetRequest request, Principal principal) {
        User currentUser = petService.loadUserByUsername(principal.getName());
        return petService.createPet(request, currentUser);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar Mascota",
            description = "Elimina una mascota por su ID. Invalida la caché del usuario.",
            responses = @ApiResponse(responseCode = "204", description = "Mascota eliminada.")
    )
    public ResponseEntity<Void> deletePet(
            @Parameter(description = "ID de la mascota a eliminar.")
            @PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/feed")
    @Operation(
            summary = "Alimentar Mascota",
            description = "Disminuye el hambre de la mascota. Invalida la caché.",
            responses = @ApiResponse(responseCode = "200", description = "Estadísticas de la mascota actualizadas.")
    )
    public ResponseEntity<PetResponse> feedPet(
            @Parameter(description = "ID de la mascota a alimentar.")
            @PathVariable Long id) {
        return ResponseEntity.ok(petService.feedPet(id));
    }

    @PostMapping("/{id}/cuddle")
    @Operation(summary = "Acariciar Mascota", description = "Aumenta la felicidad de la mascota.")
    public ResponseEntity<PetResponse> cuddlePet(@PathVariable Long id) { // Cambiado a PetResponse
        return ResponseEntity.ok(petService.cuddlePet(id));
    }

    @PostMapping("/{id}/equip")
    public ResponseEntity<PetResponse> equipAccessory(@PathVariable Long id, @RequestBody EquipRequest request) {
        return ResponseEntity.ok(petService.equipAccessory(id, request));
    }

    @PostMapping("/{id}/decrease-happiness")
    public ResponseEntity<PetResponse> decreaseHappiness(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount
    ) {
        return ResponseEntity.ok(petService.decreaseHappiness(id, amount));
    }

    @PostMapping("/{id}/increase-happiness")
    public ResponseEntity<PetResponse> increaseHappiness(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount
    ) {
        return ResponseEntity.ok(petService.increaseHappiness(id, amount));
    }

    @PostMapping("/{id}/increase-hunger")
    public ResponseEntity<PetResponse> increaseHunger(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount
    ) {
        return ResponseEntity.ok(petService.increaseHunger(id, amount));
    }

    @PostMapping("/{id}/decrease-hunger")
    public ResponseEntity<PetResponse> decreaseHunger(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int amount
    ) {
        return ResponseEntity.ok(petService.decreaseHunger(id, amount));
    }
}