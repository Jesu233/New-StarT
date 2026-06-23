package com.example.notificaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Transporta la información mínima necesaria para enviar un correo de confirmación de logout")
public class EmailLogoutDTO {

    private String destinatario;
    private String nombreUsuario;
}
