package com.virtualpet.api.controller;

import com.virtualpet.api.model.Pet; // Asegúrate de importar tu entidad Pet
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {

    // --- MÉTODOS CRUD PARA MASCOTAS ---
    // Aún no hemos creado el PetService, pero este es un adelanto de cómo se verá.
    // La lógica real irá en el servicio.

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> getAllPetsForUser() {
        // TODO: Implementar la lógica para devolver las mascotas del usuario autenticado
        // O todas si el usuario es ADMIN
        return ResponseEntity.ok("Respuesta provisional: Lista de mascotas");
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> createPet() {
        // TODO: Implementar la lógica para crear una mascota
        return ResponseEntity.ok("Respuesta provisional: Mascota creada");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> updatePet(@PathVariable Long id) {
        // TODO: Implementar la lógica para actualizar una mascota
        // Asegurarse que el USER solo puede actualizar sus propias mascotas.
        return ResponseEntity.ok("Respuesta provisional: Mascota " + id + " actualizada");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> deletePet(@PathVariable Long id) {
        // TODO: Implementar la lógica para eliminar una mascota
        // Asegurarse que el USER solo puede eliminar sus propias mascotas.
        return ResponseEntity.ok("Respuesta provisional: Mascota " + id + " eliminada");
    }
}