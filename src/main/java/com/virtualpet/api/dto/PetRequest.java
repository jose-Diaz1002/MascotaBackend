// src/main/java/com/virtualpet/api/dto/PetRequest.java
package com.virtualpet.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetRequest {
    private String name;
    private String creatureType;
    private String color;
}
