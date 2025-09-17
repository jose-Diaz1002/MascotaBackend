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

    @NotBlank(message = "El tipo de criatura no puede estar vacío")
    private String creatureType; // Ej: "Dragón", "Unicornio", etc.

    private String color;
    private String mood; // Estado de ánimo
    private int energyLevel; // Nivel de energía

    // --- Relación con User ---
    @ManyToOne(fetch = FetchType.LAZY) // JPA: Muchos pets pueden pertenecer a Un usuario.
    @JoinColumn(name = "user_id", nullable = false) // JPA: Define la columna de la clave foránea.
    private User user;
}