package com.example.notificaciones.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmailCompraDTO {

    private String destinatario;
    private String nombreUsuario;
    private String nombreEvento;
    private List<String> tokens;
}
