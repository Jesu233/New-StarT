package com.example.Tickets.assembler;

import com.example.Tickets.controller.TicketController;
import com.example.Tickets.model.Tickets;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TicketsModelAssembler implements RepresentationModelAssembler<Tickets, Tickets> {

    @Override
    public Tickets toModel(Tickets ticket) {

        ticket.add(linkTo(methodOn(TicketController.class).obtenerPorId(ticket.getIdTicket())).withSelfRel());

        ticket.add(linkTo(methodOn(TicketController.class).obtenerTodos()).withRel("todos"));

        ticket.add(linkTo(methodOn(TicketController.class).actualizarTicket(null)).withRel("actualizar"));

        ticket.add(linkTo(methodOn(TicketController.class).eliminarTicketPorId(ticket.getIdTicket())).withRel("eliminar"));

        return ticket;
    }


}