package com.example.Reservas.assembler;

import com.example.Reservas.controller.ReservasController;
import com.example.Reservas.model.Reservas;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReservasModelAssembler implements RepresentationModelAssembler<Reservas, Reservas> {

    @Override
    public Reservas toModel(Reservas reserva) {

        reserva.add(linkTo(methodOn(ReservasController.class)
                .buscarPorId(reserva.getId())).withSelfRel());

        reserva.add(linkTo(methodOn(ReservasController.class)
                .actualizarReserva(reserva.getId(), null)).withRel("actualizar"));

        reserva.add(linkTo(methodOn(ReservasController.class)
                .eliminarReserva(reserva.getId())).withRel("eliminar"));

        return reserva;
    }
}