package com.reservas.repository;

import com.reservas.model.EstadoReserva;
import com.reservas.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByUsuarioId(Long usuarioId);
    List<Reserva> findByRecursoId(Long recursoId);

    // Busca reservas del mismo recurso que se traslapan con el rango [inicio, fin).
    // Dos intervalos se traslapan cuando: uno empieza antes de que el otro termine
    // Y termina despues de que el otro empieza. Es la formula estandar para
    // detectar conflictos de horario.
    @Query("""
            SELECT r FROM Reserva r
            WHERE r.recurso.id = :recursoId
            AND r.estado = :estado
            AND r.horaInicio < :fin
            AND r.horaFin > :inicio
            """)
    List<Reserva> buscarConflictos(
            @Param("recursoId") Long recursoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin,
            @Param("estado") EstadoReserva estado
    );
}
