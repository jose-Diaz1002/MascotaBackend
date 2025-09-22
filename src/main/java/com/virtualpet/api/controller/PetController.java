package com.virtualpet.api.controller;

import com.virtualpet.api.dto.PetRequest;
import com.virtualpet.api.model.Pet; // Asegúrate de importar tu entidad Pet
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

    private final PetService petService; // Inyectamos el servicio

    // --- MÉTODOS CRUD PARA MASCOTAS ---
    // Aún no hemos creado el PetService, pero este es un adelanto de cómo se verá.
    // La lógica real irá en el servicio.

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Pet>> getAllPetsForUser() {
        // Devolvemos la lista real de mascotas
        return ResponseEntity.ok(petService.getPetsForCurrentUser());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pet> createPet(@RequestBody PetRequest petRequest) {
        // Creamos la mascota y devolvemos el objeto creado
        return ResponseEntity.ok(petService.createPet(petRequest));
    }

    // Añade estos métodos dentro de la clase PetController.java

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody PetRequest petRequest) {
        return ResponseEntity.ok(petService.updatePet(id, petRequest));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content, estándar para delete
    }

    // --- NUEVOS ENDPOINTS DE INTERACCIÓN ---
    @PostMapping("/{id}/feed")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pet> feedPet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.feedPet(id));
    }

    @PostMapping("/{id}/cuddle")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Pet> cuddlePet(@PathVariable Long id) {
        return ResponseEntity.ok(petService.cuddlePet(id));
    }
}