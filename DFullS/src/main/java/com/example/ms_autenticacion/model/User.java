package com.example.ms_autenticacion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
@Schema(description = "Entidad que representa a un usuario registrado en el sistema")
public class User extends RepresentationModel<User> {

    @Id
    @Column(nullable = false)
    @NotBlank(message = "El RUN es obligatorio")
    @Schema(
            description = "RUN del usuario (sin puntos ni guión). Es la clave primaria.",
            example = "120000000",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String run;

    @Column(nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(
            description = "Nombre del usuario",
            example = "Juan",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String nombre;

    @Column(nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    @Schema(
            description = "Apellido del usuario",
            example = "Pérez",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String apellido;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del correo es invalido")
    @Schema(
            description = "Correo electrónico único del usuario",
            example = "juan@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(
            description = "Contraseña del usuario (mínimo 6 caracteres). No se retorna en las respuestas.",
            example = "secreto123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.WRITE_ONLY
    )
    private String password;

    @Column
    @Schema(
            description = "Rol del usuario en el sistema (USER o ADMIN). Si se omite se asigna USER.",
            example = "USER",
            allowableValues = {"USER", "ADMIN"},
            accessMode = Schema.AccessMode.READ_WRITE
    )
    private String role;
}