package com.example.localidades.repository;

import com.example.localidades.model.Localidades;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocalidadesRepository extends JpaRepository<Localidades, Long> {
    Optional<Localidades> findByRecintoId(Long recintoId);
    List<Localidades> findByNombreContainingIgnoreCase(String nombre);
}