package com.example.notificaciones.dto;

import lombok.Data;

@Data
public class EmailRecuperacionDTO {

    private String destinatario;
    private String nombreUsuario;
    private String linkRecuperacion;
}
