package com.example.promocion.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promociones")
@Builder
@Schema(description = "Entidad que representa una promoción aplicable a eventos")
public class Promo extends RepresentationModel<Promo> {

    @Id
    @Size(min = 3, max = 20)
    @Schema(
            description = "Identificador único de la regla de promoción",
            example = "B_STDER",
            minLength = 3,
            maxLength = 20,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String idRegla;

    @Schema(
            description = "ID del evento al que aplica la promoción (conexión lógica)",
            example = "1001"
    )
    private Long idEvento;

    @NotBlank(message = "la descripcion es obligatoria")
    @Schema(
            description = "Descripción detallada de la promoción",
            example = "20% de descuento en compras con tarjeta Banco Estado",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(
            description = "Tipo de descuento aplicado",
            example = "PORCENTAJE",
            allowableValues = {"PORCENTAJE", "MONTO_FIJO"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private TipoDescuento tipo;

    @NotNull
    @Positive
    @Schema(
            description = "Valor del descuento (porcentaje o monto fijo)",
            example = "20.0",
            minimum = "0.01",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Double valorDescuento = 20.0;

    @Column(name = "fecha_inicio", nullable = false)
    @NotNull
    @Schema(
            description = "Fecha y hora de inicio de la promoción",
            example = "2026-06-23T00:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @NotNull
    @Schema(
            description = "Fecha y hora de término de la promoción",
            example = "2026-07-23T23:59:59",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime fechaFin;

    @Column(name = "banco_requerido")
    @Schema(
            description = "Banco requerido para aplicar la promoción (si aplica)",
            example = "Banco Estado"
    )
    private String bancoRequerido;

    @Column(name = "compania_requerida")
    @Schema(
            description = "Compañía requerida para aplicar la promoción (si aplica)",
            example = "Entel"
    )
    private String companiaRequerida;

    @Min(1)
    @Schema(
            description = "Cantidad mínima de tickets requerida para aplicar la promoción (null si no aplica)",
            example = "3",
            minimum = "1"
    )
    private Integer minTicketsReq;

    @NotNull
    @Min(1)
    @Schema(
            description = "Cantidad máxima de usos disponibles de la promoción",
            example = "100",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer stockMaximo;

    @Builder.Default
    @Schema(
            description = "Cantidad actual de usos de la promoción",
            example = "0",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Integer usosActuales = 0;

    @NotNull
    @Min(1)
    @Schema(
            description = "Límite de entradas por cliente",
            example = "5",
            minimum = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer limiteEntradasCliente;

    @PositiveOrZero
    @Schema(
            description = "Monto mínimo de compra requerido para aplicar la promoción",
            example = "15000.0",
            minimum = "0"
    )
    private Double montoMinimoRequerido;
}