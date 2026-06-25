package com.example.libro.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.libro.controller.LibroControllerV2;
import com.example.libro.dto.LibroDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LibroModelAssembler implements RepresentationModelAssembler<LibroDTO.Response, EntityModel<LibroDTO.Response>> {

    @Override
    public EntityModel<LibroDTO.Response> toModel(LibroDTO.Response libro) {
        return EntityModel.of(libro,
                linkTo(methodOn(LibroControllerV2.class).getByID(libro.getIsbn())).withSelfRel(),
                linkTo(methodOn(LibroControllerV2.class).getAllLibs()).withRel("libros"),
                linkTo(methodOn(LibroControllerV2.class).getAutoresPorLibro(libro.getIsbn())).withRel("autores"),
                linkTo(methodOn(LibroControllerV2.class).getGenero(libro.getIsbn())).withRel("genero"),
                linkTo(methodOn(LibroControllerV2.class).getEjemplares(libro.getIsbn())).withRel("ejemplares")
        );
    }
}