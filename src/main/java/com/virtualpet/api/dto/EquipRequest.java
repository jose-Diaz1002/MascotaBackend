package com.virtualpet.api.dto;

import lombok.Data;

@Data
public class EquipRequest {
    private String accessoryType;
    private String accessoryName;
}
