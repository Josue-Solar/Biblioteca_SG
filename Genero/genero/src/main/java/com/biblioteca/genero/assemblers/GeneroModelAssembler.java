package com.biblioteca.genero.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.biblioteca.genero.controller.GeneroControllerV2;
import com.biblioteca.genero.dto.GeneroDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GeneroModelAssembler implements RepresentationModelAssembler<GeneroDTO.Response, EntityModel<GeneroDTO.Response>> {

    @Override
    public EntityModel<GeneroDTO.Response> toModel(GeneroDTO.Response genero) {
        return EntityModel.of(genero,
                linkTo(methodOn(GeneroControllerV2.class).buscarPorId(genero.getId())).withSelfRel(),
                linkTo(methodOn(GeneroControllerV2.class).listar()).withRel("generos"),
                linkTo(methodOn(GeneroControllerV2.class).libroPorGenero(genero.getId())).withRel("libros-asociados")
        );
    }
}