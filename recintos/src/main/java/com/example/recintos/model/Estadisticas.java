package com.example.recintos.model;

import lombok.Data;

@Data
public class Estadisticas {
    private Long totalLocalidades;
    private Long capacidadPromedio;
    private Long mayorCapacidad;
    private Long menorCapacidad;
}