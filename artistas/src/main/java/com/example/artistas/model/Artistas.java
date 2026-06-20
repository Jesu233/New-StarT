package com.example.artistas.model;

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
@Schema(description = "Entidad que representa a un artista registrado en el sistema")
public class Artistas extends RepresentationModel<Artistas> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del artista",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 10, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Schema(
            description = "Nombre del artista",
            example = "Metallica",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private String nombre;

    @NotBlank(message = "El género no puede estar vacío")
    @Size(min = 2, max = 50, message = "El género debe tener entre 2 y 50 caracteres")
    @Schema(
            description = "Género musical del artista",
            example = "Heavy Metal",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private String genero;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(min = 2, max = 50, message = "El país debe tener entre 2 y 50 caracteres")
    @Schema(
            description = "País de origen del artista",
            example = "Estados Unidos",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE)
    private String pais;
}



