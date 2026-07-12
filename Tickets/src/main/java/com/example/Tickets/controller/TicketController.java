package com.example.Tickets.controller;

import com.example.Tickets.assembler.TicketsModelAssembler;
import com.example.Tickets.model.Tickets;
import com.example.Tickets.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
@Tag(name = "Tickets", description = "Gestión de tickets de acceso a eventos")
@SecurityRequirement(name = "bearerAuth")
public class TicketController {

    private final TicketService ticketService;
    private final TicketsModelAssembler assembler;

    @GetMapping
    @Operation(
            summary = "Listar tickets",
            description = "Obtiene todos los tickets registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado")
    })
    public ResponseEntity<CollectionModel<Tickets>> obtenerTodos() {
        List<Tickets> tickets = ticketService.findAll();

        CollectionModel<Tickets> modelo = assembler.toCollectionModel(tickets);
        modelo.add(linkTo(methodOn(TicketController.class).obtenerTodos()).withSelfRel());
        modelo.add(linkTo(methodOn(TicketController.class).crearTicket(null)).withRel("crear"));

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/{idTicket}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Buscar ticket por ID",
            description = "Busca un ticket a través de su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket encontrado"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<Tickets> obtenerPorId(@PathVariable Long idTicket) {
        return ticketService.findById(idTicket)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/evento/{idEvento}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Buscar tickets por evento",
            description = "Obtiene los tickets asociados a un evento en particular"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<CollectionModel<Tickets>> obtenerPorEvento(@PathVariable Long idEvento) {
        List<Tickets> tickets = ticketService.findByEvento(idEvento);

        CollectionModel<Tickets> modelo = assembler.toCollectionModel(tickets);
        modelo.add(linkTo(methodOn(TicketController.class).obtenerPorEvento(idEvento)).withSelfRel());
        modelo.add(linkTo(methodOn(TicketController.class).obtenerTodos()).withRel("todos"));
        modelo.add(linkTo(methodOn(TicketController.class).crearTicket(null)).withRel("crear"));

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/tipo/{tipoTicket}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Buscar tickets por tipo",
            description = "Obtiene los tickets según su tipo (VIP, GENERAL, PREMIUM, ESTUDIANTE)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<CollectionModel<Tickets>> obtenerPorTipo(@PathVariable String tipoTicket) {
        List<Tickets> tickets = ticketService.findByTipo(tipoTicket);

        CollectionModel<Tickets> modelo = assembler.toCollectionModel(tickets);
        modelo.add(linkTo(methodOn(TicketController.class).obtenerPorTipo(tipoTicket)).withSelfRel());
        modelo.add(linkTo(methodOn(TicketController.class).obtenerTodos()).withRel("todos"));
        modelo.add(linkTo(methodOn(TicketController.class).crearTicket(null)).withRel("crear"));

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/precio/menor/{precio}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Buscar tickets con precio menor",
            description = "Obtiene los tickets cuyo precio es menor al valor indicado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<CollectionModel<Tickets>> obtenerPorPrecioMenor(@PathVariable int precio) {
        List<Tickets> tickets = ticketService.findByPrecioLessThan(precio);

        CollectionModel<Tickets> modelo = assembler.toCollectionModel(tickets);
        modelo.add(linkTo(methodOn(TicketController.class).obtenerPorPrecioMenor(precio)).withSelfRel());
        modelo.add(linkTo(methodOn(TicketController.class).obtenerTodos()).withRel("todos"));

        return ResponseEntity.ok(modelo);
    }

    @GetMapping("/stock/mayor/{stock}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Operation(
            summary = "Buscar tickets con stock mayor",
            description = "Obtiene los tickets cuyo stock disponible es mayor al valor indicado"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<CollectionModel<Tickets>> obtenerPorStockMayor(@PathVariable int stock) {
        List<Tickets> tickets = ticketService.findByStockGreaterThan(stock);

        CollectionModel<Tickets> modelo = assembler.toCollectionModel(tickets);
        modelo.add(linkTo(methodOn(TicketController.class).obtenerPorStockMayor(stock)).withSelfRel());
        modelo.add(linkTo(methodOn(TicketController.class).obtenerTodos()).withRel("todos"));

        return ResponseEntity.ok(modelo);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Crear ticket",
            description = "Crea un nuevo ticket en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos")
    })
    public ResponseEntity<Tickets> crearTicket(@Valid @RequestBody Tickets ticket) {
        Tickets creado = ticketService.save(ticket);
        Tickets modelo = assembler.toModel(creado);

        URI location = linkTo(methodOn(TicketController.class).obtenerPorId(creado.getIdTicket())).toUri();

        return ResponseEntity.created(location).body(modelo);
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Actualizar ticket",
            description = "Actualiza los datos de un ticket existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ticket actualizado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<Tickets> actualizarTicket(@Valid @RequestBody Tickets ticket) {
        Tickets actualizado = ticketService.update(ticket);
        return ResponseEntity.ok(assembler.toModel(actualizado));
    }



    @DeleteMapping("/{idTicket}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
            summary = "Eliminar ticket por ID",
            description = "Elimina un ticket a través de su identificador"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ticket eliminado correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Ticket no encontrado")
    })
    public ResponseEntity<Void> eliminarTicketPorId(@PathVariable Long idTicket) {
        ticketService.deleteById(idTicket);

        URI location = linkTo(methodOn(TicketController.class).obtenerTodos()).toUri();
        return ResponseEntity.noContent().location(location).build();
    }
}