package com.example.recintos.controller;

import com.example.recintos.client.RecintosClient;
import com.example.recintos.model.Estadisticas;
import com.example.recintos.model.Recintos;
import com.example.recintos.service.RecintosService;
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
public class RecintosController {

    private final RecintosService service;
    private final RecintosClient client;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Recintos> listar() {
        log.info("GET /api/v1/recintos - Listando recintos");
        return service.listar();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Recintos> buscarPorNombre(@RequestParam String nombre) {
        log.info("GET /api/v1/recintos/buscar - nombre: {}", nombre);
        return service.buscarPorNombre(nombre);
    }

    @GetMapping("/localidades/estadisticas")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
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
    public ResponseEntity<Recintos> obtener(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token) {
        log.info("GET /api/v1/recintos/{} - Obteniendo recinto", id);
        return service.obtener(id)
                .map(recinto -> {
                    recinto.setLocalidades(client.obtenerLocalidades(recinto.getId(), token));
                    return ResponseEntity.ok(recinto);
                })
                .orElseGet(() -> {
                    log.warn("Recinto con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Recintos> guardar(@Valid @RequestBody Recintos recinto) {
        log.info("POST /api/v1/recintos - Creando recinto: {}", recinto.getNombre());
        return ResponseEntity.status(201).body(service.guardar(recinto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Recintos> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Recintos recinto) {
        log.info("PUT /api/v1/recintos/{} - Actualizando recinto", id);
        return service.actualizar(id, recinto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Recinto con ID {} no encontrado para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/recintos/{} - Eliminando recinto", id);
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}