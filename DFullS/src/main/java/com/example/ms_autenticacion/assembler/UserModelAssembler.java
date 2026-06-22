package com.example.ms_autenticacion.assembler;

import com.example.ms_autenticacion.controller.UserController;
import com.example.ms_autenticacion.model.User;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, User> {

    @Override
    public User toModel(User user) {


        user.add(linkTo(methodOn(UserController.class)
                .getByRun(user.getRun())).withSelfRel());


        user.add(linkTo(methodOn(UserController.class)
                .update(user.getRun(), null)).withRel("actualizar"));


        user.add(linkTo(methodOn(UserController.class)
                .delete(user.getRun())).withRel("eliminar"));

        return user;
    }
}