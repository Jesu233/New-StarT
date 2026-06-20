package com.example.artistas.assembler;

import com.example.artistas.controller.ArtistasController;
import com.example.artistas.model.Artistas;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ArtistasModelAssembler implements RepresentationModelAssembler<Artistas, Artistas> {

    @Override
    public Artistas toModel(Artistas artista) {

        artista.add(linkTo(methodOn(ArtistasController.class).obtener(artista.getId())).withSelfRel());

        artista.add(linkTo(methodOn(ArtistasController.class).listar()).withRel("todos"));

        artista.add(linkTo(methodOn(ArtistasController.class).actualizar(artista.getId(), null)).withRel("actualizar"));

        artista.add(linkTo(methodOn(ArtistasController.class).eliminar(artista.getId())).withRel("eliminar"));

        return artista;
    }
}
