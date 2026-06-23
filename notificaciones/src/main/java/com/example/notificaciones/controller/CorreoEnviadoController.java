package com.example.notificaciones.controller;

import com.example.notificaciones.model.CorreoEnviado;
import com.example.notificaciones.repository.CorreoEnviadoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/correos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Consultas de Correos Enviados", description = "API para consultar el historial de correos enviados")
@SecurityRequirement(name = "bearerAuth")
public class CorreoEnviadoController {

    private final CorreoEnviadoRepository correoRepo;

    @GetMapping
    @Operation(summary = "Obtener todos los correos enviados")
    public ResponseEntity<CollectionModel<CorreoEnviado>> findAll() {
        log.info("GET /correos - Obteniendo todos los correos");
        List<CorreoEnviado> correos = correoRepo.findAll();

        // Agregar enlaces HATEOAS a cada correo
        for (CorreoEnviado correo : correos) {
            correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                    .findById(correo.getId())).withSelfRel());
        }

        // ✅ Envolver en CollectionModel con enlace a la colección
        CollectionModel<CorreoEnviado> model = CollectionModel.of(correos);
        model.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findAll()).withSelfRel());
        model.add(linkTo(methodOn(CorreoEnviadoController.class)
                .getEstadisticas()).withRel("estadisticas"));

        return ResponseEntity.ok(model);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un correo específico por su ID")
    public ResponseEntity<CorreoEnviado> findById(@PathVariable Long id) {
        log.info("GET /correos/{} - Buscando correo", id);

        return correoRepo.findById(id)
                .map(correo -> {
                    // Agregar enlaces HATEOAS
                    correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                            .findById(id)).withSelfRel());
                    correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                            .findAll()).withRel("todos"));
                    correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                            .findByDestinatario(correo.getDestinatario()))
                            .withRel("buscarPorDestinatario"));
                    correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                            .findByTipo(correo.getTipo()))
                            .withRel("buscarPorTipo"));
                    correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                            .findByEstado(correo.getEstado()))
                            .withRel("buscarPorEstado"));

                    return ResponseEntity.ok(correo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/destinatario/{email}")
    @Operation(summary = "Buscar correos por destinatario")
    public ResponseEntity<CollectionModel<CorreoEnviado>> findByDestinatario(@PathVariable String email) {
        log.info("GET /correos/destinatario/{} - Buscando correos", email);
        List<CorreoEnviado> correos = correoRepo.findByDestinatario(email);

        for (CorreoEnviado correo : correos) {
            correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                    .findById(correo.getId())).withSelfRel());
        }

        CollectionModel<CorreoEnviado> model = CollectionModel.of(correos);
        model.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findByDestinatario(email)).withSelfRel());

        return ResponseEntity.ok(model);
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar correos por tipo (COMPRA, LOGOUT, BIENVENIDA, RECUPERACION)")
    public ResponseEntity<CollectionModel<CorreoEnviado>> findByTipo(@PathVariable String tipo) {
        log.info("GET /correos/tipo/{} - Buscando correos por tipo", tipo);
        List<CorreoEnviado> correos = correoRepo.findByTipo(tipo);

        for (CorreoEnviado correo : correos) {
            correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                    .findById(correo.getId())).withSelfRel());
        }

        CollectionModel<CorreoEnviado> model = CollectionModel.of(correos);
        model.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findByTipo(tipo)).withSelfRel());

        return ResponseEntity.ok(model);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Buscar correos por estado (ENVIADO, FALLIDO)")
    public ResponseEntity<CollectionModel<CorreoEnviado>> findByEstado(@PathVariable String estado) {
        log.info("GET /correos/estado/{} - Buscando correos por estado", estado);
        List<CorreoEnviado> correos = correoRepo.findByEstado(estado);

        for (CorreoEnviado correo : correos) {
            correo.add(linkTo(methodOn(CorreoEnviadoController.class)
                    .findById(correo.getId())).withSelfRel());
        }

        CollectionModel<CorreoEnviado> model = CollectionModel.of(correos);
        model.add(linkTo(methodOn(CorreoEnviadoController.class)
                .findByEstado(estado)).withSelfRel());

        return ResponseEntity.ok(model);
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de correos enviados")
    public ResponseEntity<Map<String, Object>> getEstadisticas() {
        log.info("GET /correos/estadisticas - Generando estadísticas");
        List<CorreoEnviado> todos = correoRepo.findAll();

        long total = todos.size();
        long enviados = todos.stream().filter(c -> "ENVIADO".equals(c.getEstado())).count();
        long fallidos = todos.stream().filter(c -> "FALLIDO".equals(c.getEstado())).count();

        Map<String, Long> porTipo = todos.stream()
                .collect(Collectors.groupingBy(CorreoEnviado::getTipo, Collectors.counting()));

        Map<String, Object> stats = Map.of(
                "total", total,
                "enviados", enviados,
                "fallidos", fallidos,
                "porTipo", porTipo
        );

        return ResponseEntity.ok(stats);
    }
}