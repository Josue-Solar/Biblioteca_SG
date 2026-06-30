package com.biblioteca.persona.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.biblioteca.persona.controller.PersonaControllerV2;
import com.biblioteca.persona.dto.PersonaDTO;

@Component
public class PersonaModelAssembler implements RepresentationModelAssembler<PersonaDTO.Response, EntityModel<PersonaDTO.Response>>{

    @Override
    public EntityModel<PersonaDTO.Response> toModel(PersonaDTO.Response persona) {
        // Retorna el DTO envuelto en un EntityModel junto con sus enlaces dinámicos
        return EntityModel.of(persona,
                // Link "self": Enlace directo a este género específico basado en su ID
                linkTo(methodOn(PersonaControllerV2.class).buscarPorId(persona.getId())).withSelfRel(),
                // Link "generos": Enlace de retorno hacia la colección completa v2
                linkTo(methodOn(PersonaControllerV2.class).listarTodos()).withRel("personas")
        );
    }

}
