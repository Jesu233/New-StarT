package com.example.promocion.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar el uso exitoso de una promoción")
public class RegistroUsoDTO {

    @Schema(
            description = "Identificador de la regla de promoción usada",
            example = "B_STDER",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String idRegla;

    @Schema(
            description = "ID del evento asociado",
            example = "1001",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idEvento;

    @Schema(
            description = "RUN del usuario que usó la promoción",
            example = "12345678-9",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String userRun;

    @Schema(
            description = "Cantidad de tickets comprados con descuento",
            example = "4",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer cantidadTickets;

    @Schema(
            description = "ID único de la reserva asociada",
            example = "RES-2026-06-23-ABC123",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String reservaId;
}