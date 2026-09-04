package com.reservas.service;

import com.reservas.model.Servicio;
import com.reservas.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    public Servicio guardar(Servicio servicio) {
        return servicioRepository.save(servicio);
    }
}
