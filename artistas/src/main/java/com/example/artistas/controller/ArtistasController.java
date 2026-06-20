package com.example.artistas.controller;

import com.example.artistas.assembler.ArtistasModelAssembler;
import com.example.artistas.model.Artistas;
import com.example.artistas.service.ArtistasService;
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
@RequestMapping("/api/v1/artistas")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Gestión de Artistas StarTicket")
public class ArtistasController {

    private final ArtistasService service;
    private final ArtistasModelAssembler assembler;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Listar artistas",
            description = "Retorna todos los artistas registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public List<Artistas> listar() {
        log.info("GET /api/v1/artistas - Listando artistas");
        return service.listar().stream()
                .map(assembler::toModel)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar artista por ID",
            description = "Busca un artista a través de su ID en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Artistas> obtener(@PathVariable Long id) {
        log.info("GET /api/v1/artistas/{} - Obteniendo artista", id);
        return service.obtener(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Artista con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Buscar artistas",
            description = "Busca artistas por nombre o género"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public List<Artistas> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String genero) {

        if (nombre != null) {
            log.info("GET /api/v1/artistas/buscar?nombre={}", nombre);
            return service.buscarPorNombre(nombre).stream()
                    .map(assembler::toModel)
                    .toList();
        } else if (genero != null) {
            log.info("GET /api/v1/artistas/buscar?genero={}", genero);
            return service.buscarPorGenero(genero).stream()
                    .map(assembler::toModel)
                    .toList();
        }
        return service.listar().stream()
                .map(assembler::toModel)
                .toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Crear artista",
            description = "Crea un nuevo artista en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Artista creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Artistas> guardar(@Valid @RequestBody Artistas artista) {
        log.info("POST /api/v1/artistas - Creando artista: {}", artista.getNombre());
        return ResponseEntity.status(201).body(assembler.toModel(service.guardar(artista)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Actualizar artista",
            description = "Actualiza un artista existente en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Artistas> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Artistas artista) {
        log.info("PUT /api/v1/artistas/{} - Actualizando artista", id);
        return service.actualizar(id, artista)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Artista con ID {} no encontrado para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(
            summary = "Eliminar artista",
            description = "Elimina un artista del sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Artista no encontrado"),
            @ApiResponse(responseCode = "500", description = "Fallo del servidor")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/artistas/{} - Eliminando artista", id);
        return service.eliminar(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
