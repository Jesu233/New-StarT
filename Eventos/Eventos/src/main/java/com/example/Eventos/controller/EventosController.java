package com.example.Eventos.controller;

import com.example.Eventos.assembler.EventosModelAssembler;
import com.example.Eventos.model.Eventos;
import com.example.Eventos.service.EventosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/evento")
@SecurityRequirement(name = "bearerAuth")
public class EventosController {


    @Autowired
    private EventosService eventosService;

    @Autowired
    private EventosModelAssembler assembler;


    @GetMapping
    public ResponseEntity<List<Eventos>> listarTodo(){
        List<Eventos> eventos = eventosService.listarTodo();


        List<Eventos> eventosConLinks = eventos.stream()
                .map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(eventosConLinks);
    }

    @GetMapping("/{id}/capacidad")
    public ResponseEntity<Long> obtenerCapacidad(@PathVariable Long id) {
        Eventos evento = eventosService.buscarPorId(id);
        return ResponseEntity.ok(evento.getCapacidad());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Crear evento",
            description = "Crea un evento evento en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Evento creado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    public ResponseEntity<Eventos> agregarEvento(@Valid @RequestBody Eventos eventos) {
        Eventos evento = eventosService.agregarEvento(eventos);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(evento));
    }



    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "eliminar evento",
            description = "Elimina un evento evento en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Evento eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        eventosService.eliminarEvento(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "actualizar evento",
            description = "Actualiza un evento evento en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evento actualizado correctamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    public ResponseEntity<Eventos> actualizarEvento(@PathVariable Long id, @Valid @RequestBody Eventos eventos) {
        Eventos evento = eventosService.actualizarEvento(id, eventos);
        return ResponseEntity.ok(assembler.toModel(evento));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "buscar por ID",
            description = "busca un evento a través de su ID en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evento encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    public ResponseEntity<Eventos> buscarPorId(@PathVariable Long id) {
        Eventos evento = eventosService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(evento));
    }


    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "buscar por nombre",
            description = "busca un evento a través de su nombre en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Evento encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No autenticado"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Sin permisos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Fallo del servidor"
            )



    })
    public ResponseEntity<Eventos> buscarPorNombre(@PathVariable String nombre) {
        Eventos evento = eventosService.buscarPorNombre(nombre);
        return ResponseEntity.ok(assembler.toModel(evento));
    }



}