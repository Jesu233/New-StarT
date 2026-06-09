package com.example.Reservas.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


@Data
public class Eventos {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Se necesita el nombre del evento.")
    private String nombre;

    @NotNull (message = "Ingrese la capacidad.")
    private Long capacidad;

    @NotNull (message = "El evento necesita una fecha.")
    private LocalDateTime fechaEvento;

    @NotNull (message = "EL evento necesita un lugar.")
    private String LugarEvento;


}
