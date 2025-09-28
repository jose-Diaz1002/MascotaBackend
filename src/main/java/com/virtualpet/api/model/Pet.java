// src/main/java/com/virtualpet/api/model/Pet.java
package com.virtualpet.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime; // 1. Importa

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

    private String name;
    private String creatureType; // Ej: "Mascota Base"
    private String color;        // Ej: "#FFA500" (naranja)
    private int hunger;
    private int happiness;
    // 2. Añade este nuevo campo
    private LocalDateTime lastUpdated;

    // --- NUEVOS CAMPOS PARA ACCESORIOS ---
    // Guardaremos un identificador para cada tipo de accesorio.
    // Son 'nullable' porque la mascota puede no tenerlos equipados.
    @Column(nullable = true)
    private String hat;

    @Column(nullable = true)
    private String hairstyle;

    @Column(nullable = true)
    private String shirt;

    @Column(nullable = true)
    private String pants;

    @Column(nullable = true)
    private String background;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}