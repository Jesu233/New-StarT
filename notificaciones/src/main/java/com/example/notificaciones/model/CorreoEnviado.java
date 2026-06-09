package com.example.notificaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "correos_enviados")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorreoEnviado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destinatario;
    private String tipo; // COMPRA, LOGOUT, BIENVENIDA, RECUPERACION
    private String estado; // ENVIADO, FALLIDO
    private LocalDateTime fechaEnvio = LocalDateTime.now();
}
