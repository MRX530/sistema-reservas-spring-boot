package com.reservas.service;

import com.reservas.model.*;
import com.reservas.repository.ReservaRepository;
import com.reservas.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ServicioRepository servicioRepository;

    /**
     * Crea una reserva, calculando la hora de fin a partir de la duracion
     * del servicio, y validando que el Recurso no tenga ya otra reserva
     * CONFIRMADA que se traslape con ese horario.
     *
     * Esta es la regla de negocio central del proyecto: a diferencia del
     * inventario (donde se valida una cantidad) o finanzas (donde solo se
     * alerta), aqui SI se bloquea la operacion -- dos reservas del mismo
     * recurso en el mismo horario simplemente no pueden coexistir.
     */
    @Transactional
    public Reserva crearReserva(Long usuarioId, Long servicioId, Long recursoId, LocalDateTime horaInicio, Usuario usuario, Recurso recurso) {
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        LocalDateTime horaFin = horaInicio.plusMinutes(servicio.getDuracionMinutos());

        List<Reserva> conflictos = reservaRepository.buscarConflictos(
                recursoId, horaInicio, horaFin, EstadoReserva.CONFIRMADA);

        if (!conflictos.isEmpty()) {
            throw new ConflictoHorarioException(
                    "El recurso ya tiene una reserva entre " + conflictos.get(0).getHoraInicio() +
                    " y " + conflictos.get(0).getHoraFin());
        }

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setServicio(servicio);
        reserva.setRecurso(recurso);
        reserva.setHoraInicio(horaInicio);
        reserva.setHoraFin(horaFin);
        reserva.setEstado(EstadoReserva.CONFIRMADA);

        return reservaRepository.save(reserva);
    }

    public void cancelar(Long reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);
    }

    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    public List<Reserva> listarPorRecurso(Long recursoId) {
        return reservaRepository.findByRecursoId(recursoId);
    }
}
