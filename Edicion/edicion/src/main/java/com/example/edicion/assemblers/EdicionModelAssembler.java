package com.example.edicion.assemblers;

import com.example.edicion.controller.EdicionControllerV2;
import com.example.edicion.dto.EdicionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class EdicionModelAssembler implements RepresentationModelAssembler<EdicionDTO.Response, EntityModel<EdicionDTO.Response>> {

    @Override
    public EntityModel<EdicionDTO.Response> toModel(EdicionDTO.Response edicion) {
        return EntityModel.of(edicion,
                linkTo(methodOn(EdicionControllerV2.class).buscarPorId(edicion.getId())).withSelfRel(),
                linkTo(methodOn(EdicionControllerV2.class).listarTodas()).withRel("ediciones"),
                linkTo(methodOn(EdicionControllerV2.class).librosPorEdicion(edicion.getId())).withRel("libros"));
    }
}
