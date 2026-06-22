package com.example.Reservas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Entidad que representa los cupos de los eventos vendidos")
public class Reservas extends RepresentationModel<Reservas> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Identificador único de la reserva",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank
    @Size(min = 4, max = 50)
    @Schema(
            description = "Nombre de la reserva",
            example = "Reserva VIP Cosmódromo",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    @NotNull
    @Schema(
            description = "Fecha y hora de inicio de la reserva",
            example = "2026-08-15T10:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @NotNull
    @Schema(
            description = "Fecha y hora de fin de la reserva",
            example = "2026-08-15T12:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private LocalDateTime fechaFin;

    @NotBlank
    @Schema(
            description = "Tipo de reserva (ej. VIP, GENERAL, PALCO)",
            example = "VIP",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String tipo;

    @NotBlank
    @Schema(
            description = "Nombre o identificador del cliente que realiza la reserva",
            example = "Juan Pérez",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String cliente;

    @NotBlank
    @Schema(
            description = "Estado actual de la reserva (ej. CONFIRMADA, PENDIENTE, CANCELADA)",
            example = "CONFIRMADA",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String estado;
}