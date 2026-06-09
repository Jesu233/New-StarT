package com.example.Reservas.controller;

import com.example.Reservas.client.EventosClient;
import com.example.Reservas.model.Reservas;
import com.example.Reservas.service.ReservasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@RequestMapping("/api/v1/reserva")
@RequiredArgsConstructor
public class ReservasController {

    private final EventosClient eventosClient;
    private final ReservasService reservasService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Reservas guardarReserva(@Valid @RequestBody Reservas reservas){
        return reservasService.guardarReserva(reservas);}

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public void eliminarReserva(@PathVariable Long id) {
        reservasService.eliminarReserva(id);}

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Reservas actualizarReserva(@PathVariable Long id, Reservas reservas){
        return reservasService.actualizarReserva(id,reservas);}

    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Reservas buscarPorId(@PathVariable Long id){
        return reservasService.buscarPorId(id);}

    @GetMapping("{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public Reservas buscarPorNombre(@PathVariable String nombre){
        return reservasService.buscarPorNombre(nombre);}


}
