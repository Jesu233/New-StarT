package com.example.localidades.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Schema(description = "Entidad que representa que cosas se considerarán para las estadísticas estadísticas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estadisticas {


    private Long totalLocalidades;

    @Schema(description = "capacidad aproximada en las localidades registradas")
    private Long capacidadPromedio;

    @Schema(description = "Mayor capacidad que se encuentra en las localidades")
    private Long mayorCapacidad;

    @Schema(description = "Menor capacidad que se encuentra en las localidades")
    private Long menorCapacidad;
}