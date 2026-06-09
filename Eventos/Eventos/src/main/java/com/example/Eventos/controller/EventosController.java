package com.example.Eventos.controller;

import com.example.Eventos.model.Eventos;
import com.example.Eventos.service.EventosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/evento")
public class EventosController {


    @Autowired
    private EventosService eventosService;


    @GetMapping
    public ResponseEntity<List<Eventos>> listarTodo() {
        return ResponseEntity.ok(eventosService.listarTodo());
    }

    @GetMapping("/{id}/capacidad")
    public ResponseEntity<Long> obtenerCapacidad(@PathVariable Long id) {
        Eventos evento = eventosService.buscarPorId(id);
        return ResponseEntity.ok(evento.getCapacidad());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Eventos> agregarEvento(@Valid @RequestBody Eventos eventos) {
        Eventos nuevoEvento = eventosService.agregarEvento(eventos);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEvento);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        eventosService.eliminarEvento(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Eventos> actualizarEvento(@PathVariable Long id, @Valid @RequestBody Eventos eventos) {
        Eventos eventoActualizado = eventosService.actualizarEvento(id, eventos);
        return ResponseEntity.ok(eventoActualizado);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Eventos> buscarPorId(@PathVariable Long id) {
        Eventos evento = eventosService.buscarPorId(id);
        return ResponseEntity.ok(evento);
    }


    @GetMapping("/nombre/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<Eventos> buscarPorNombre(@PathVariable String nombre) {
        Eventos evento = eventosService.buscarPorNombre(nombre);
        return ResponseEntity.ok(evento);
    }



}