package com.example.Reservas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reservas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    @NotNull
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @NotNull
    private LocalDateTime fechaFin;

    @NotNull
    private String tipo;

    @NotNull
    private String cliente;



    private String estado;









}
