package com.example.Eventos.assembler;


import com.example.Eventos.controller.EventosController;
import com.example.Eventos.model.Eventos;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EventosModelAssembler implements RepresentationModelAssembler<Eventos, Eventos> {


    @Override
    public Eventos toModel(Eventos evento) {


        evento.add(linkTo(methodOn(EventosController.class).buscarPorId(evento.getId())).withSelfRel());


        evento.add(linkTo(methodOn(EventosController.class).listarTodo()).withRel("todos"));


        evento.add(linkTo(methodOn(EventosController.class).actualizarEvento(evento.getId(), null)).withRel("actualizar"));


        evento.add(linkTo(methodOn(EventosController.class).eliminarEvento(evento.getId())).withRel("eliminar"));

        evento.add(linkTo(methodOn(EventosController.class).obtenerCapacidad(evento.getId())).withRel("obtener capacidad"));



        return evento;
    }
}






