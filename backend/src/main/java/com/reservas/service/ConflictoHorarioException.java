package com.reservas.service;

public class ConflictoHorarioException extends RuntimeException {
    public ConflictoHorarioException(String mensaje) {
        super(mensaje);
    }
}
