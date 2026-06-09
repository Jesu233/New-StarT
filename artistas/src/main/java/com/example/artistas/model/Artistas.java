package com.example.artistas.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artistas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 10, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    private String nombre;

    @NotBlank(message = "El género no puede estar vacío")
    @Size(min = 2, max = 50, message = "El género debe tener entre 2 y 50 caracteres")
    private String genero;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(min = 2, max = 50, message = "El país debe tener entre 2 y 50 caracteres")
    private String pais;
}