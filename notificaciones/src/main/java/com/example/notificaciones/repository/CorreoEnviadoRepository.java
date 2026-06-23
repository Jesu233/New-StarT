package com.example.notificaciones.repository;

import com.example.notificaciones.model.CorreoEnviado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorreoEnviadoRepository extends JpaRepository<CorreoEnviado, Long> {

    //metodo para buscar x destinatario
    List<CorreoEnviado> findByDestinatario(String destinatario);

    //metodo para buscar x tipo
    List<CorreoEnviado> findByTipo(String tipo);

    //metodo para buscar x estado
    List<CorreoEnviado> findByEstado(String estado);
}
