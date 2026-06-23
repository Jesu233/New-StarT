package com.example.notificaciones.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "correos_enviados")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Registra y persiste informacion sobre los correo electrónicos que el sistema ha intentado enviar")
public class CorreoEnviado extends RepresentationModel<CorreoEnviado> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinatario;
    private String tipo; // COMPRA, LOGOUT, BIENVENIDA, RECUPERACION
    private String estado; // ENVIADO, FALLIDO
    private LocalDateTime fechaEnvio = LocalDateTime.now();
}
