package com.example.localidades.assembler;

import com.example.localidades.controller.LocalidadesController;
import com.example.localidades.model.Localidades;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class LocalidadesModelAssembler implements RepresentationModelAssembler<Localidades,Localidades> {

    @Override
    public Localidades toModel(Localidades localidad) {

        localidad.add(linkTo(methodOn(LocalidadesController.class).actualizar(localidad.getId(), null)).withRel("actualizar"));


        return localidad;
    }

}
