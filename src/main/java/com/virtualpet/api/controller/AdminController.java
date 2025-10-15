package com.virtualpet.api.controller;

import com.virtualpet.api.dto.PetResponse;
import com.virtualpet.api.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PetService petService;

    /**
     * GET /api/admin/pets - Obtiene todas las mascotas del sistema (solo ADMIN)
     */
    @GetMapping("/pets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PetResponse>> getAllPets() {
        List<PetResponse> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    /**
     * DELETE /api/admin/pets/{id} - Elimina una mascota (solo ADMIN)
     */
    @DeleteMapping("/pets/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}