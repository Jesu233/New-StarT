package com.example.recintos.service;

import com.example.recintos.model.Recintos;
import com.example.recintos.repository.RecintosRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RecintosService {

    @Autowired
    private RecintosRepository repository;

    public List<Recintos> listar() {
        log.info("Listando todos los recintos");
        return repository.findAll();
    }

    public Optional<Recintos> obtener(Long id) {
        log.info("Buscando recinto con ID {}", id);
        return repository.findById(id);
    }

    public List<Recintos> buscarPorNombre(String nombre) {
        log.info("Buscando recintos con nombre: {}", nombre);
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public Recintos guardar(Recintos recinto) {
        log.info("Guardando recinto: {}", recinto.getNombre());
        return repository.save(recinto);
    }

    public Optional<Recintos> actualizar(Long id, Recintos recinto) {
        log.info("Actualizando recinto con ID {}", id);
        return repository.findById(id).map(existente -> {
            existente.setNombre(recinto.getNombre());
            existente.setUbicacion(recinto.getUbicacion());
            existente.setCapacidad(recinto.getCapacidad());
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        log.info("Eliminando recinto con ID {}", id);
        return repository.findById(id).map(recinto -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }
}