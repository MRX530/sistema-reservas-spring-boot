package com.reservas.controller;

import com.reservas.model.Recurso;
import com.reservas.model.Reserva;
import com.reservas.model.Usuario;
import com.reservas.repository.RecursoRepository;
import com.reservas.repository.UsuarioRepository;
import com.reservas.service.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RecursoRepository recursoRepository;

    @PostMapping
    public Reserva crear(@RequestBody ReservaRequest request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Recurso recurso = recursoRepository.findById(request.getRecursoId())
                .orElseThrow(() -> new RuntimeException("Recurso no encontrado"));

        return reservaService.crearReserva(
                request.getUsuarioId(),
                request.getServicioId(),
                request.getRecursoId(),
                request.getHoraInicio(),
                usuario,
                recurso
        );
    }

    @PutMapping("/{id}/cancelar")
    public void cancelar(@PathVariable Long id) {
        reservaService.cancelar(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Reserva> listarPorUsuario(@PathVariable Long usuarioId) {
        return reservaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/recurso/{recursoId}")
    public List<Reserva> listarPorRecurso(@PathVariable Long recursoId) {
        return reservaService.listarPorRecurso(recursoId);
    }
}
