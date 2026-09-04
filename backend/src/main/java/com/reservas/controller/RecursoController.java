package com.reservas.controller;

import com.reservas.model.Recurso;
import com.reservas.service.RecursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recursos")
@CrossOrigin(origins = "*")
public class RecursoController {

    @Autowired
    private RecursoService recursoService;

    @GetMapping
    public List<Recurso> listar() {
        return recursoService.listarTodos();
    }

    @PostMapping
    public Recurso crear(@RequestBody Recurso recurso) {
        return recursoService.guardar(recurso);
    }
}
