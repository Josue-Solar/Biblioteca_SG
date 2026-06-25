package com.biblioteca.editorial.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.biblioteca.editorial.controller.EditorialControllerV2;
import com.biblioteca.editorial.dto.EditorialDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EditorialModelAssembler implements RepresentationModelAssembler<EditorialDTO.Response, EntityModel<EditorialDTO.Response>> {

    @Override
    public EntityModel<EditorialDTO.Response> toModel(EditorialDTO.Response editorial) {
        // Creamos el modelo envolviendo el DTO y añadiendo los enlaces hipermedia (HATEOAS)
        return EntityModel.of(editorial,
                // Enlace "self": apunta al detalle de esta editorial específica (/api/v2/editoriales/{id})
                linkTo(methodOn(EditorialControllerV2.class).buscarPorId(editorial.getId())).withSelfRel(),
                
                // Enlace "editoriales": apunta a la colección completa para poder regresar a la lista
                linkTo(methodOn(EditorialControllerV2.class).listar()).withRel("editoriales")
        );
    }
}