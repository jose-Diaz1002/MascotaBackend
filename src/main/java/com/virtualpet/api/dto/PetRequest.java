package com.virtualpet.api.dto;

import lombok.Data;

@Data
public class PetRequest {
    private String name;
    private String color;
}