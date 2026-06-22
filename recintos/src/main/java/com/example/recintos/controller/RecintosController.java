package com.example.recintos.controller;

import com.example.recintos.assembler.RecintosModelAssembler;
import com.example.recintos.client.RecintosClient;
import com.example.recintos.model.Estadisticas;
import com.example.recintos.model.Recintos;
import com.example.recintos.service.RecintosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recintos")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gestión de Recintos StarTicket")
public class RecintosController {

    private final RecintosService service;
    private final RecintosClient client;
    private final RecintosModelAssembler assembler;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Listar recintos",
            description = "Retorna todos los recintos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public List<Recintos> listar() {
        log.info("GET /api/v1/recintos - Listando recintos");
        return service.listar().stream()
                .map(assembler::toModel)
                .toList();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar recintos por nombre",
            description = "Busca recintos cuyo nombre contenga el texto indicado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public List<Recintos> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/v1/recintos/buscar - nombre: {}", nombre);
        return service.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .toList();
    }

    @GetMapping("/localidades/estadisticas")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Obtener estadísticas de localidades",
            description = "Consulta al microservicio de Localidades y retorna estadísticas generales"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "No se pudieron obtener las estadísticas"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Estadisticas> estadisticasLocalidades(
            @RequestHeader("Authorization") String token) {
        log.info("GET /api/v1/recintos/localidades/estadisticas");
        Estadisticas estadisticas = client.obtenerEstadisticas(token);
        return estadisticas != null
                ? ResponseEntity.ok(estadisticas)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar recinto por ID",
            description = "Busca un recinto por su ID e incluye sus localidades asociadas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recinto encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recinto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Recintos> obtener(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        log.info("GET /api/v1/recintos/{} - Obteniendo recinto", id);
        return service.obtener(id)
                .map(recinto -> {
                    recinto.setLocalidades(client.obtenerLocalidades(recinto.getId(), token));
                    return ResponseEntity.ok(assembler.toModel(recinto));
                })
                .orElseGet(() -> {
                    log.warn("Recinto con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Crear recinto",
            description = "Crea un nuevo recinto en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recinto creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Recintos> guardar(@Valid @RequestBody Recintos recinto) {
        log.info("POST /api/v1/recintos - Creando recinto: {}", recinto.getNombre());
        return ResponseEntity.status(201).body(assembler.toModel(service.guardar(recinto)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Actualizar recinto",
            description = "Actualiza un recinto existente en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recinto actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recinto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Recintos> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Recintos recinto) {
        log.info("PUT /api/v1/recintos/{} - Actualizando recinto", id);
        return service.actualizar(id, recinto)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Recinto con ID {} no encontrado para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Eliminar recinto",
            description = "Elimina un recinto del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Recinto eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Recinto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/recintos/{} - Eliminando recinto", id);
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}