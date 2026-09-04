package com.reservas.controller;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservaRequest {
    private Long usuarioId;
    private Long servicioId;
    private Long recursoId;
    private LocalDateTime horaInicio;
}
