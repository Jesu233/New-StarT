package com.example.notificaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Contiene la informacion necesaria para personalizar y enviar un correo de bienvenida a un nuevo usuario")
public class EmailBienvenidaDTO {

    private String destinatario;
    private String nombreUsuario;
}
