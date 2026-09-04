package com.reservas.service;

import com.reservas.model.Recurso;
import com.reservas.repository.RecursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoService {

    @Autowired
    private RecursoRepository recursoRepository;

    public List<Recurso> listarTodos() {
        return recursoRepository.findAll();
    }

    public Recurso guardar(Recurso recurso) {
        return recursoRepository.save(recurso);
    }
}
