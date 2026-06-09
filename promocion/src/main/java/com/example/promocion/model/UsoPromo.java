package com.example.promocion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "uso_promociones")
public class UsoPromo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idRegla;
    private Long idEvento;
    private String userRun;
    private Integer cantidadTickets;
    private String reservaId;
    private LocalDateTime fechaUso = LocalDateTime.now();
}
