package com.virtualpet.api.dto;

import com.virtualpet.api.model.Pet;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private String color;
    private int hunger;
    private int happiness;
    private String ownerUsername;

    private String hat;
    private String hairstyle;
    private String shirt;
    private String pants;
    private String background;
    private LocalDateTime lastUpdated;

    public static PetResponse fromEntity(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .color(pet.getColor())
                .hunger(pet.getHunger())
                .happiness(pet.getHappiness())
                .ownerUsername(pet.getUser().getUsername())
                .hat(pet.getHat())
                .hairstyle(pet.getHairstyle())
                .shirt(pet.getShirt())
                .pants(pet.getPants())
                .background(pet.getBackground())
                .build();
    }
}