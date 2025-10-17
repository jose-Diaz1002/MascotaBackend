package com.virtualpet.api.controller;

import com.virtualpet.api.dto.PetResponse;
import com.virtualpet.api.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Administración de Sistema (ADMIN)", description = "Endpoints de alto nivel, solo accesibles para usuarios con rol 'ROLE_ADMIN'.")
public class AdminController {

    private final PetService petService;

    /**
     * GET /api/admin/pets - Obtiene todas las mascotas del sistema (solo ADMIN)
     */
    @GetMapping("/pets")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Listar TODAS las mascotas del sistema",
            description = "Devuelve una lista de todas las mascotas creadas por todos los usuarios. Requiere autenticación y rol ADMIN.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista completa de mascotas."),
                    @ApiResponse(responseCode = "403", description = "Prohibido: No tiene rol ADMIN."),
                    @ApiResponse(responseCode = "401", description = "No autorizado: Token JWT inválido/ausente.")
            }
    )
    public ResponseEntity<List<PetResponse>> getAllPets() {
        List<PetResponse> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    /**
     * DELETE /api/admin/pets/{id} - Elimina una mascota (solo ADMIN)
     */
    @DeleteMapping("/pets/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Eliminar Mascota del Sistema",
            description = "Elimina permanentemente una mascota por su ID, independientemente de su dueño. Requiere rol ADMIN. **Invalida la caché del dueño**.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Mascota eliminada con éxito (No Content)."),
                    @ApiResponse(responseCode = "403", description = "Prohibido: No tiene rol ADMIN."),
                    @ApiResponse(responseCode = "404", description = "Mascota no encontrada."),
                    @ApiResponse(responseCode = "401", description = "No autorizado: Token JWT inválido/ausente.")
            }
    )
    public ResponseEntity<Void> deletePet(
            @Parameter(description = "ID único de la mascota a eliminar.")
            @PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}