package com.virtualpet.api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private String creatureType;
    private String color;
    private int hunger;
    private int happiness;
    private LocalDateTime lastUpdated;

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