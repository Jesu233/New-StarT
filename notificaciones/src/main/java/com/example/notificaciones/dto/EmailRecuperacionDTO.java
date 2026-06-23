package com.example.notificaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Transporta la información necesaria para enviar un correo con un enlace de restablecimiento de contraseña")
public class EmailRecuperacionDTO {

    private String destinatario;
    private String nombreUsuario;
    private String linkRecuperacion;
}
