package com.example.Reservas.controller;

import com.example.Reservas.assembler.ReservasModelAssembler;
import com.example.Reservas.client.EventosClient;
import com.example.Reservas.model.Reservas;
import com.example.Reservas.service.ReservasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reserva")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReservasController {

    private final EventosClient eventosClient;
    private final ReservasService reservasService;
    private final ReservasModelAssembler assembler;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Crear reserva",
            description = "Crea una nueva reserva en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reserva creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Reservas> guardarReserva(@Valid @RequestBody Reservas reservas) {
        Reservas guardada = reservasService.guardarReserva(reservas);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(guardada));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Eliminar reserva",
            description = "Elimina una reserva existente por su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reserva eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservasService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Actualizar reserva",
            description = "Actualiza los datos de una reserva existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Reservas> actualizarReserva(@PathVariable Long id,
                                                      @Valid @RequestBody Reservas reservas) {
        Reservas actualizada = reservasService.actualizarReserva(id, reservas);
        return ResponseEntity.ok(assembler.toModel(actualizada));
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Buscar reserva por ID",
            description = "Retorna una reserva específica según su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Reservas> buscarPorId(@PathVariable Long id) {
        Reservas reserva = reservasService.buscarPorId(id);
        return ResponseEntity.ok(assembler.toModel(reserva));
    }


    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Buscar reserva por nombre",
            description = "Retorna una reserva específica según su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Reservas> buscarPorNombre(@PathVariable String nombre) {
        Reservas reserva = reservasService.buscarPorNombre(nombre);
        return ResponseEntity.ok(assembler.toModel(reserva));
    }
}