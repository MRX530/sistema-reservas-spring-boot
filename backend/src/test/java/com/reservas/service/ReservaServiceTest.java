package com.reservas.service;

import com.reservas.model.*;
import com.reservas.repository.ReservaRepository;
import com.reservas.repository.ServicioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ReservaService reservaService;

    private Servicio servicio;
    private Usuario usuario;
    private Recurso recurso;

    @BeforeEach
    void setUp() {
        servicio = new Servicio();
        servicio.setId(1L);
        servicio.setNombre("Corte de cabello");
        servicio.setDuracionMinutos(30);
        servicio.setPrecio(15.0);

        usuario = new Usuario();
        usuario.setId(1L);

        recurso = new Recurso();
        recurso.setId(1L);
        recurso.setNombre("Estilista Juan");
    }

    @Test
    void reservaSinConflictos_seCreaCorrectamente() {
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 10, 10, 0);

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(reservaRepository.buscarConflictos(eq(1L), any(), any(), eq(EstadoReserva.CONFIRMADA)))
                .thenReturn(List.of()); // sin conflictos
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Reserva resultado = reservaService.crearReserva(1L, 1L, 1L, inicio, usuario, recurso);

        assertEquals(inicio, resultado.getHoraInicio());
        assertEquals(inicio.plusMinutes(30), resultado.getHoraFin()); // duracion del servicio
        assertEquals(EstadoReserva.CONFIRMADA, resultado.getEstado());
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    void reservaConConflicto_lanzaExcepcionYNoGuarda() {
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 10, 10, 0);

        Reserva existente = new Reserva();
        existente.setHoraInicio(LocalDateTime.of(2026, 9, 10, 9, 45));
        existente.setHoraFin(LocalDateTime.of(2026, 9, 10, 10, 15));

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        when(reservaRepository.buscarConflictos(eq(1L), any(), any(), eq(EstadoReserva.CONFIRMADA)))
                .thenReturn(List.of(existente)); // se traslapa con la nueva reserva

        ConflictoHorarioException ex = assertThrows(ConflictoHorarioException.class, () ->
                reservaService.crearReserva(1L, 1L, 1L, inicio, usuario, recurso)
        );

        assertTrue(ex.getMessage().contains("ya tiene una reserva"));
        verify(reservaRepository, never()).save(any());
    }

    @Test
    void reservaJustoDespuesDeOtra_noEsConflicto() {
        // La reserva existente termina justo cuando empieza la nueva -> no deberia chocar
        LocalDateTime inicio = LocalDateTime.of(2026, 9, 10, 10, 30);

        when(servicioRepository.findById(1L)).thenReturn(Optional.of(servicio));
        // El repositorio (con la query real) no devolveria conflicto en este caso;
        // simulamos ese comportamiento correcto devolviendo lista vacia.
        when(reservaRepository.buscarConflictos(eq(1L), any(), any(), eq(EstadoReserva.CONFIRMADA)))
                .thenReturn(List.of());
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        Reserva resultado = reservaService.crearReserva(1L, 1L, 1L, inicio, usuario, recurso);

        assertNotNull(resultado);
        verify(reservaRepository).save(any());
    }

    @Test
    void servicioInexistente_lanzaExcepcion() {
        when(servicioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                reservaService.crearReserva(1L, 99L, 1L, LocalDateTime.now(), usuario, recurso)
        );
    }

    @Test
    void cancelar_cambiaElEstadoACancelada() {
        Reserva reserva = new Reserva();
        reserva.setId(5L);
        reserva.setEstado(EstadoReserva.CONFIRMADA);

        when(reservaRepository.findById(5L)).thenReturn(Optional.of(reserva));
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(inv -> inv.getArgument(0));

        reservaService.cancelar(5L);

        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());
        verify(reservaRepository).save(reserva);
    }
}
