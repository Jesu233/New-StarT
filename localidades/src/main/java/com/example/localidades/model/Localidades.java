package com.example.localidades.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Localidades {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;

    @NotNull(message = "Campo delantero no puede estar vacío")
    @Min(value = 1, message = "Cantidad de campo delantero debe ser mayor a 0")
    private Long campoDelantero;

    @NotNull(message = "Campo trasero no puede estar vacío")
    @Min(value = 1, message = "Cantidad de campo trasero debe ser mayor a 0")
    private Long campoTrasero;

    @NotNull(message = "Platea baja no puede estar vacía")
    @Min(value = 1, message = "Cantidad de platea baja debe ser mayor a 0")
    private Long plateaBaja;

    @NotNull(message = "Platea alta no puede estar vacía")
    @Min(value = 1, message = "Cantidad de platea alta debe ser mayor a 0")
    private Long plateaAlta;

    @NotNull(message = "El recinto Id no puede estar vacío")
    @Min(value = 1, message = "El recinto Id debe ser mayor a 0")
    @Column(name = "recinto_id")
    private Long recintoId;
}