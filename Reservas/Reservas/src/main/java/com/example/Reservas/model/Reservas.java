package com.example.Reservas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Entidad que representa los cupos de los eventos vendidos" )
public class Reservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la reserva",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 50)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    @NotNull
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @NotNull
    private LocalDateTime fechaFin;

    @NotBlank
    private String tipo;

    @NotBlank
    private String cliente;


    @NotBlank
    private String estado;









}
