package com.example.promocion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "promociones")
@Builder
public class Promo {

    @Id
    @Size(min = 3, max = 20)
    private String idRegla; // "B_STDER", "T_ENTEL", "3_CANT"

    private Long idEvento; //conexion logica con eventos

    @NotBlank(message = "la descripcion es obligatoria")
    private String descripcion;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDescuento tipo;   //porcentaje o montofijo

    @NotNull
    @Positive
    private Double valorDescuento= 20.0;


    @Column(name= "fecha_inicio", nullable = false)
    @NotNull
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @NotNull
    private LocalDateTime fechaFin;

    // reglas
    @Column(name = "banco_requerido")
    private String bancoRequerido;

    @Column(name= "compañia_requerida")
    private String companiaRequerida;

    @Min(1)
    private Integer minTicketsReq; // null si no es por cantidad

    //limites

    @NotNull
    @Min(1)
    private Integer stockMaximo;

    @Builder.Default
    private Integer usosActuales = 0;

    @NotNull
    @Min(1)
    private Integer limiteEntradasCliente;

    @PositiveOrZero
    private Double montoMinimoRequerido;


}
