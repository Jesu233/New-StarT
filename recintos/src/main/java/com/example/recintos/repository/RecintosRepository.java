package com.example.recintos.repository;

import com.example.recintos.model.Recintos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecintosRepository extends JpaRepository<Recintos, Long> {
    List<Recintos> findByNombreContainingIgnoreCase(String nombre);
}