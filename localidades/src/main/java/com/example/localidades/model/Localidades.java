package com.example.localidades.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una localidad registrada en el sistema")
public class Localidades extends RepresentationModel<Localidades> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la localidad",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(
            description = "Nombre de la localidad",
            example = "Platea Norte",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private String nombre;

    @NotNull(message = "Campo delantero no puede estar vacío")
    @Min(value = 1, message = "Cantidad de campo delantero debe ser mayor a 0")
    @Schema(
            description = "Capacidad del sector campo delantero",
            example = "1500",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private Long campoDelantero;

    @NotNull(message = "Campo trasero no puede estar vacío")
    @Min(value = 1, message = "Cantidad de campo trasero debe ser mayor a 0")
    @Schema(
            description = "Capacidad del sector campo trasero",
            example = "1500",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private Long campoTrasero;

    @NotNull(message = "Platea baja no puede estar vacía")
    @Min(value = 1, message = "Cantidad de platea baja debe ser mayor a 0")
    @Schema(
            description = "Capacidad del sector platea baja",
            example = "800",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private Long plateaBaja;

    @NotNull(message = "Platea alta no puede estar vacía")
    @Min(value = 1, message = "Cantidad de platea alta debe ser mayor a 0")
    @Schema(
            description = "Capacidad del sector platea alta",
            example = "600",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private Long plateaAlta;

    @NotNull(message = "El recinto Id no puede estar vacío")
    @Min(value = 1, message = "El recinto Id debe ser mayor a 0")
    @Column(name = "recinto_id")
    @Schema(
            description = "ID del recinto al que pertenece esta localidad",
            example = "3",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private Long recintoId;
}
