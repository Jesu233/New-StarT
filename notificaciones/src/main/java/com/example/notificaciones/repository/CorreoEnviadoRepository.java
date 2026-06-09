package com.example.notificaciones.repository;

import com.example.notificaciones.model.CorreoEnviado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CorreoEnviadoRepository extends JpaRepository<CorreoEnviado, Long> {

}
