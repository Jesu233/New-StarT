package com.example.artistas.service;

import com.example.artistas.model.Artistas;
import com.example.artistas.repository.ArtistasRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ArtistasService {

    @Autowired
    private ArtistasRepository repository;

    public List<Artistas> listar() {
        log.info("Listando todos los artistas");
        return repository.findAll();
    }

    public Optional<Artistas> obtener(Long id) {
        log.info("Buscando artista con ID {}", id);
        return repository.findById(id);
    }

    public List<Artistas> buscarPorNombre(String nombre) {
        log.info("Buscando artistas con nombre: {}", nombre);
        return repository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Artistas> buscarPorGenero(String genero) {
        log.info("Buscando artistas con genero: {}", genero);
        return repository.findByGeneroContainingIgnoreCase(genero);
    }

    public Artistas guardar(Artistas artista) {
        log.info("Guardando artista: {}", artista.getNombre());
        return repository.save(artista);
    }

    public Optional<Artistas> actualizar(Long id, Artistas artista) {
        log.info("Actualizando artista con ID {}", id);
        return repository.findById(id).map(existente -> {
            existente.setNombre(artista.getNombre());
            existente.setGenero(artista.getGenero());
            existente.setPais(artista.getPais());
            return repository.save(existente);
        });
    }

    public boolean eliminar(Long id) {
        log.info("Eliminando artista con ID {}", id);
        return repository.findById(id).map(artista -> {
            repository.deleteById(id);
            return true;
        }).orElse(false);
    }
}