package com.example.promocion.dto;

import lombok.Data;

@Data
public class CalculoPromoRequest {

    private String idRegla;
    private Long idEvento;
    private String userRun;
    private Integer cantidadTickets;
    private Double montoTotal;

}
