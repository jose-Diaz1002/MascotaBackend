// src/main/java/com/virtualpet/api/dto/PetResponse.java
package com.virtualpet.api.dto;

import com.virtualpet.api.model.Pet;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private String color;
    private int hunger;
    private int sadness;
    private String ownerUsername;

    // --- NUEVOS CAMPOS PARA ACCESORIOS ---
    private String hat;
    private String hairstyle;
    private String shirt;
    private String pants;
    private String background;

    public static PetResponse fromEntity(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .color(pet.getColor())
                .hunger(pet.getHunger())
                .sadness(pet.getSadness())
                .ownerUsername(pet.getUser().getUsername())
                .hat(pet.getHat())
                .hairstyle(pet.getHairstyle())
                .shirt(pet.getShirt())
                .pants(pet.getPants())
                .background(pet.getBackground())
                .build();
    }
}