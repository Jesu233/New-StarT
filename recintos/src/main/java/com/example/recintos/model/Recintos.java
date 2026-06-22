package com.example.recintos.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Recintos extends RepresentationModel<Recintos> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La ubicación no puede estar vacía")
    @Size(min = 3, max = 100, message = "La ubicación debe tener entre 3 y 100 caracteres")
    private String ubicacion;

    @NotNull(message = "La capacidad no puede estar vacía")
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Long capacidad;

    @Transient
    private Localidades localidades;
}