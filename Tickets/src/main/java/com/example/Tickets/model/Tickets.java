package com.example.Tickets.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "ticketsList", itemRelation = "ticket")
@Schema(
        description = "Entidad que representa un ticket de entrada a un evento",
        title = "Ticket"
)
public class Tickets extends RepresentationModel<Tickets> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "ID único del ticket",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long idTicket;

    @Schema(
            description = "ID del evento al que pertenece el ticket",
            example = "10",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idEvento;

    @NotNull(message = "Se necesita el nombre del ticket.")
    @Schema(
            description = "Nombre del ticket",
            example = "Entrada VIP",
            requiredMode = Schema.RequiredMode.REQUIRED,
            maxLength = 100
    )
    private String nombre;

    @NotNull(message = "Se necesita descripción")
    @Schema(
            description = "Descripción detallada del ticket",
            example = "Acceso a zona VIP con beneficios exclusivos",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String descripcion;

    @NotNull(message = "Se necesita fecha.")
    @Schema(
            description = "Fecha y hora de generación del ticket",
            example = "2026-06-23T10:00:00",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private LocalDateTime fechaGenerado;

    @NotNull(message = "Se necesita precio.")
    @Schema(
            description = "Precio del ticket en pesos chilenos",
            example = "15000",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int precio;

    @NotNull(message = "Ingrese un tipo")
    @Schema(
            description = "Tipo de ticket",
            example = "VIP",
            allowableValues = {"VIP", "GENERAL", "PREMIUM", "ESTUDIANTE"},
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipoTicket;
    @NotNull
    @Schema(
            description = "Cantidad de tickets disponibles en stock",
            example = "100",
            minimum = "0",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int stock;
}