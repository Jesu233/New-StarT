package com.example.localidades.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estadisticas {
    private Long totalLocalidades;
    private Long capacidadPromedio;
    private Long mayorCapacidad;
    private Long menorCapacidad;
}