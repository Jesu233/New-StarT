package com.example.artistas.repository;

import com.example.artistas.model.Artistas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistasRepository extends JpaRepository<Artistas, Long> {
    List<Artistas> findByNombreContainingIgnoreCase(String nombre);
    List<Artistas> findByGeneroContainingIgnoreCase(String genero);
}