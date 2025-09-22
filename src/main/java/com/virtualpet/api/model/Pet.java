package com.virtualpet.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de la mascota no puede estar vacío")
    private String name;

    private String creatureType; // Lo dejamos para el tipo "Humo"

    private String color;

    private String specialFeatures; // Campo nuevo

    private int hunger; // Campo nuevo (reemplaza energyLevel)

    private int sadness; // Campo nuevo (reemplaza mood)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}