package com.reservas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "recursos")
@Data
public class Recurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Generico a proposito: puede ser un profesional, una mesa, un consultorio,
    // una cancha -- lo que el negocio necesite reservar.
    @Column(nullable = false)
    private String nombre;

    private String descripcion;
}
