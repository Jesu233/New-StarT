package com.example.localidades.controller;

import com.example.localidades.model.Estadisticas;
import com.example.localidades.model.Localidades;
import com.example.localidades.service.LocalidadesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Gestion de Localidades StarTicket")
public class LocalidadesController {

    @Autowired
    private LocalidadesService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Localidades> listar() {
        log.info("GET /api/v1/localidades - Listando localidades");
        return service.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Localidades> obtener(@PathVariable Long id) {
        log.info("GET /api/v1/localidades/{} - Obteniendo localidad", id);
        return service.obtener(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Localidad con ID {} no encontrada", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/recintos/{recintoId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Localidades> obtenerPorRecinto(@PathVariable Long recintoId) {
        log.info("GET /api/v1/localidades/recintos/{} - Obteniendo localidad por recinto", recintoId);
        return service.obtenerPorRecintoId(recintoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Localidad con recintoId {} no encontrada", recintoId);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Localidades> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/v1/localidades/buscar - nombre: {}", nombre);
        return service.buscarPorNombre(nombre);
    }

    @GetMapping("/{id}/total")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    public ResponseEntity<Estadisticas> estadisticas() {
        log.info("GET /api/v1/localidades/estadisticas");
        return ResponseEntity.ok(service.obtenerEstadisticas());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Crear/Ingresar nueva localidad")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Localidad creada exitosamente"),
    })
    public ResponseEntity<Localidades> guardar(@Valid @RequestBody Localidades localidad) {
        log.info("POST /api/v1/localidades - Creando localidad: {}", localidad.getNombre());
        return ResponseEntity.status(201).body(service.guardar(localidad));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Localidades> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Localidades localidad) {
        log.info("PUT /api/v1/localidades/{} - Actualizando localidad", id);
        return service.actualizar(id, localidad)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Localidad con ID {} no encontrada para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/localidades/{} - Eliminando localidad", id);
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}