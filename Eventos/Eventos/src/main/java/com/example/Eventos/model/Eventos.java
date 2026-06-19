package com.example.Eventos.model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Entidad que representa a un evento registrado en el sistema")
public class Eventos {


    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    @Schema(description = "Identificador único del evento",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Size(min = 4, max = 50)
    @NotNull (message = "Se necesita el nombre del evento.")
    @Schema(
            description = "Nombre del evento",
            example = "Cosmódromo",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String nombre;


    @NotNull (message = "Ingrese la capacidad.")
    @Schema(
            description = "Capacidad total del evento",
            example = "4000",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE
    )
    private Long capacidad;

    @NotNull (message = "El evento necesita una fecha.")
    @Column(name = "fecha_evento")
    @Schema(
            description = "Fecha y hora en que se realizará el evento",
            example = "2026-08-15T20:00:00",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE
    )
    private LocalDateTime fechaEvento;

    @NotNull (message = "EL evento necesita un lugar.")
    @Column(name = "lugar_evento")
    @Max(50)
    @Schema (
            description = "Lugar donde se realizará el evento",
            example = "Estadio Nacional",
            requiredMode = Schema.RequiredMode.REQUIRED, accessMode = Schema.AccessMode.READ_WRITE)
    private String lugarEvento;



}
