package com.example.promocion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "uso_promociones")
@Schema(description = "Registro de uso de promociones por parte de los usuarios")
public class UsoPromo extends RepresentationModel<UsoPromo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Identificador único del uso de promoción",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

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

    @Schema(
            description = "Fecha y hora del uso de la promoción",
            example = "2026-06-23T14:30:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime fechaUso = LocalDateTime.now();
}
