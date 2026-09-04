package com.reservas.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "servicios")
@Data
public class Servicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // "Corte de cabello", "Consulta general", etc.

    @Column(nullable = false)
    private Integer duracionMinutos;

    @Column(nullable = false)
    private Double precio;
}
