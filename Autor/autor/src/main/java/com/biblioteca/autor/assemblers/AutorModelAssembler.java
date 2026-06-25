package com.biblioteca.autor.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.biblioteca.autor.controller.AutorControllerV2;
import com.biblioteca.autor.dto.AutorDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AutorModelAssembler implements RepresentationModelAssembler<AutorDTO.Response, EntityModel<AutorDTO.Response>>{
    @Override
    public EntityModel<AutorDTO.Response> toModel(AutorDTO.Response autor) {
        // Retorna el DTO envuelto en un EntityModel junto con sus enlaces dinámicos
        return EntityModel.of(autor,
                // Link "self": Enlace directo a este género específico basado en su ID
                linkTo(methodOn(AutorControllerV2.class).buscarPorId(autor.getId())).withSelfRel(),
                // Link "autores": Enlace de retorno hacia la colección completa v2
                linkTo(methodOn(AutorControllerV2.class).listarTodos()).withRel("autores")
        );
    }

}
