package com.example.Tickets.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name ="tickets")
@NoArgsConstructor
@AllArgsConstructor
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_ticket;
    private Long id_evento;

    @NotNull(message = "Se necesita el nombre del ticket.")
    private String nombre;

    @NotNull(message = "Se necesita descripción")
    private String descripcion;

    @NotNull(message = "Se necesita fecha.")
    private LocalDateTime fecha_generado;

    @NotNull(message = "Se necesita precio.")
    private int precio;

    @NotNull(message = "Ingrese un tipo")
    private String tipo_ticket;

    @NotNull
    private int stock;


}