// src/main/java/com/virtualpet/api/dto/PetResponse.java
package com.virtualpet.api.dto;

import com.virtualpet.api.model.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetResponse {
    // Solo los campos que el frontend necesita ver
    private Long id;
    private String name;
    private String creatureType;
    private String color;
    private String specialFeatures;
    private int hunger;
    private int sadness;
    private String ownerUsername; // En lugar de todo el objeto User, solo su nombre

    // Un método útil para convertir una entidad Pet a un PetResponse DTO
    public static PetResponse fromEntity(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .creatureType(pet.getCreatureType())
                .color(pet.getColor())
                .specialFeatures(pet.getSpecialFeatures())
                .hunger(pet.getHunger())
                .sadness(pet.getSadness())
                .ownerUsername(pet.getUser().getUsername()) // Obtenemos solo el nombre
                .build();
    }
}