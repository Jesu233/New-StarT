package com.example.recintos.assembler;

import com.example.recintos.controller.RecintosController;
import com.example.recintos.model.Recintos;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class RecintosModelAssembler implements RepresentationModelAssembler<Recintos, Recintos> {

    @Override
    public Recintos toModel(Recintos recinto) {

        recinto.add(linkTo(methodOn(RecintosController.class).obtener(recinto.getId(), null)).withSelfRel());

        recinto.add(linkTo(methodOn(RecintosController.class).listar()).withRel("todos"));

        recinto.add(linkTo(methodOn(RecintosController.class).actualizar(recinto.getId(), null)).withRel("actualizar"));

        recinto.add(linkTo(methodOn(RecintosController.class).eliminar(recinto.getId())).withRel("eliminar"));

        return recinto;
    }
}