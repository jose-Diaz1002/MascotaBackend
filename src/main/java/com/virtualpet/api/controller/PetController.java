package com.virtualpet.api.controller;

import com.virtualpet.api.dto.EquipRequest;
import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.dto.PetResponse; // Asegúrate de importar PetResponse
import com.virtualpet.api.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<PetResponse>> getPets() {
        return ResponseEntity.ok(petService.getPetsForCurrentUser());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PetResponse>> getAllPetsForAdmin() { // Cambiado a List<PetResponse>
        return ResponseEntity.ok(petService.getAllPets());
    }

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@RequestBody PetRequest request) {
        return ResponseEntity.ok(petService.createPet(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<PetResponse> updatePet(@PathVariable Long id, @RequestBody PetRequest request) { // Cambiado a PetResponse
        return ResponseEntity.ok(petService.updatePet(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/feed")
    public ResponseEntity<PetResponse> feedPet(@PathVariable Long id) { // Cambiado a PetResponse
        return ResponseEntity.ok(petService.feedPet(id));
    }

    @PostMapping("/{id}/cuddle")
    public ResponseEntity<PetResponse> cuddlePet(@PathVariable Long id) { // Cambiado a PetResponse
        return ResponseEntity.ok(petService.cuddlePet(id));
    }

    // Dentro de la clase PetController.java

    // --- NUEVO ENDPOINT UNIFICADO ---
    @PostMapping("/{id}/equip")
    public ResponseEntity<PetResponse> equipAccessory(@PathVariable Long id, @RequestBody EquipRequest request) {
        return ResponseEntity.ok(petService.equipAccessory(id, request));
    }
}