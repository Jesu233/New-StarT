package com.example.Reservas.repository;

import com.example.Reservas.model.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservasRepository extends JpaRepository<Reservas, Long> {

    Optional<Reservas> findByNombre(String nombre);
}
