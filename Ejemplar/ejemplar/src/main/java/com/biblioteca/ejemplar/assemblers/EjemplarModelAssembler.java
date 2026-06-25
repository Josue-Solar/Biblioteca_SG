package com.biblioteca.ejemplar.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.biblioteca.ejemplar.controller.EjemplarControllerV2;
import com.biblioteca.ejemplar.model.Ejemplar;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EjemplarModelAssembler implements RepresentationModelAssembler<Ejemplar, EntityModel<Ejemplar>> {

    @Override
    public EntityModel<Ejemplar> toModel(Ejemplar ejemplar) {
        return EntityModel.of(ejemplar,
                // Enlace al detalle del ejemplar individual (self)
                linkTo(methodOn(EjemplarControllerV2.class).buscarPorId(ejemplar.getId())).withSelfRel(),
                
                // Enlace de regreso a la lista global de ejemplares
                linkTo(methodOn(EjemplarControllerV2.class).listar()).withRel("ejemplares")
        );
    }
}