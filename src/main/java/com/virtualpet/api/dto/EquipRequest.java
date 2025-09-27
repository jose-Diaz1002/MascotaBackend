// src/main/java/com/virtualpet/api/dto/EquipRequest.java
package com.virtualpet.api.dto;

import lombok.Data;

@Data
public class EquipRequest {
    private String accessoryType; // "hat", "shirt", "pants", etc.
    private String accessoryName; // "gorra", "camiseta-rayas", etc.
}
