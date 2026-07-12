package com.example.Eventos.repository;


import com.example.Eventos.model.Eventos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventosRepository extends JpaRepository<Eventos, Long> {

    Optional<Eventos> findByNombre(String nombre);

    Optional<Eventos> findByLugarEvento    (String lugar_evento);
}
