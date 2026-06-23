package com.example.notificaciones.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Transporta toda la información necesaria para enviar un correo de confirmación de compra")
public class EmailCompraDTO  {

    private String destinatario;
    private String nombreUsuario;
    private String nombreEvento;
    private List<String> tokens;
}
