package com.example.Eventos.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Eventos {


    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private Long id;

    @NotNull (message = "Se necesita el nombre del evento.")
    private String nombre;

    @NotNull (message = "Ingrese la capacidad.")
    private Long capacidad;

    @NotNull (message = "El evento necesita una fecha.")
    @Column(name = "fecha_evento")
    private LocalDateTime fechaEvento;

    @NotNull (message = "EL evento necesita un lugar.")
    @Column(name = "lugar_evento")
    private String lugarEvento;



}
