package com.example.artistas.controller;

import com.example.artistas.model.Artistas;
import com.example.artistas.service.ArtistasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/artistas")
@RequiredArgsConstructor
@Slf4j
public class ArtistasController {

    private final ArtistasService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Artistas> listar() {
        log.info("GET /api/v1/artistas - Listando artistas");
        return service.listar();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Artistas> obtener(@PathVariable Long id) {
        log.info("GET /api/v1/artistas/{} - Obteniendo artista", id);
        return service.obtener(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Artista con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Artistas> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String genero) {

        if (nombre != null) {
            log.info("GET /api/v1/artistas/buscar?nombre={}", nombre);
            return service.buscarPorNombre(nombre);
        } else if (genero != null) {
            log.info("GET /api/v1/artistas/buscar?genero={}", genero);
            return service.buscarPorGenero(genero);
        }
        return service.listar();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Artistas> guardar(@Valid @RequestBody Artistas artista) {
        log.info("POST /api/v1/artistas - Creando artista: {}", artista.getNombre());
        return ResponseEntity.status(201).body(service.guardar(artista));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Artistas> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Artistas artista) {
        log.info("PUT /api/v1/artistas/{} - Actualizando artista", id);
        return service.actualizar(id, artista)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Artista con ID {} no encontrado para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/artistas/{} - Eliminando artista", id);
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}