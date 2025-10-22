package com.virtualpet.api.dto;

import lombok.Data;

@Data
public class PetRequest {
    private String name;
    private int hunger;
    private int happiness;
    private String color;

}