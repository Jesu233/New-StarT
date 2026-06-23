package com.example.promocion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Solicitud para calcular el descuento aplicable a una compra")
public class CalculoPromoRequest {

    @Schema(
            description = "Identificador de la regla de promoción",
            example = "B_STDER",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String idRegla;

    @Schema(
            description = "ID del evento al que aplica la promoción",
            example = "1001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idEvento;

    @Schema(
            description = "RUN del usuario que realiza la compra",
            example = "12345678-9",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userRun;

    @Schema(
            description = "Cantidad de tickets que se desean comprar",
            example = "4",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidadTickets;

    @Schema(
            description = "Monto total de la compra antes de aplicar descuento",
            example = "80000.0",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double montoTotal;
}