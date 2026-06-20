package com.example.localidades.controller;

import com.example.localidades.assembler.LocalidadesModelAssembler;
import com.example.localidades.model.Estadisticas;
import com.example.localidades.model.Localidades;
import com.example.localidades.service.LocalidadesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/localidades")
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gestión de Localidades StarTicket")
public class LocalidadesController {

    @Autowired
    private LocalidadesService service;

    @Autowired
    private LocalidadesModelAssembler assembler;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Listar localidades",
            description = "Retorna todas las localidades registradas en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public List<Localidades> listar() {
        log.info("GET /api/v1/localidades - Listando localidades");
        return service.listar().stream()
                .map(assembler::toModel)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar localidad por ID",
            description = "Busca una localidad a través de su ID en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localidad encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Localidades> obtener(@PathVariable Long id) {
        log.info("GET /api/v1/localidades/{} - Obteniendo localidad", id);
        return service.obtener(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Localidad con ID {} no encontrada", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/recintos/{recintoId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar localidad por recinto",
            description = "Busca una localidad a través del ID de su recinto"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localidad encontrada"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada para ese recinto"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Localidades> obtenerPorRecinto(@PathVariable Long recintoId) {
        log.info("GET /api/v1/localidades/recintos/{} - Obteniendo localidad por recinto", recintoId);
        return service.obtenerPorRecintoId(recintoId)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Localidad con recintoId {} no encontrada", recintoId);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar localidades por nombre",
            description = "Busca localidades cuyo nombre contenga el texto indicado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public List<Localidades> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/v1/localidades/buscar - nombre: {}", nombre);
        return service.buscarPorNombre(nombre).stream()
                .map(assembler::toModel)
                .toList();
    }

    @GetMapping("/{id}/total")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Calcular capacidad total",
            description = "Calcula la capacidad total sumando todos los sectores de la localidad"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Capacidad calculada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Map<String, Long>> capacidadTotal(@PathVariable Long id) {
        log.info("GET /api/v1/localidades/{}/total - Calculando capacidad total", id);
        return service.calcularCapacidadTotal(id)
                .map(total -> ResponseEntity.ok(Map.of("capacidadTotal", total)))
                .orElseGet(() -> {
                    log.warn("Localidad con ID {} no encontrada para calcular total", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/estadisticas")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Obtener estadísticas",
            description = "Retorna estadísticas generales de todas las localidades registradas"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Estadisticas> estadisticas() {
        log.info("GET /api/v1/localidades/estadisticas");
        return ResponseEntity.ok(service.obtenerEstadisticas());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Crear localidad",
            description = "Crea una nueva localidad en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Localidad creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Localidades> guardar(@Valid @RequestBody Localidades localidad) {
        log.info("POST /api/v1/localidades - Creando localidad: {}", localidad.getNombre());
        return ResponseEntity.status(201).body(assembler.toModel(service.guardar(localidad)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Actualizar localidad",
            description = "Actualiza una localidad existente en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Localidad actualizada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Localidades> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Localidades localidad) {
        log.info("PUT /api/v1/localidades/{} - Actualizando localidad", id);
        return service.actualizar(id, localidad)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Localidad con ID {} no encontrada para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Eliminar localidad",
            description = "Elimina una localidad del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Localidad eliminada correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Localidad no encontrada"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/localidades/{} - Eliminando localidad", id);
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
