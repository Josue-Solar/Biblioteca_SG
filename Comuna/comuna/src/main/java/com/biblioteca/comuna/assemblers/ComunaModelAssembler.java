package com.biblioteca.comuna.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.biblioteca.comuna.controller.ComunaControllerV2;
import com.biblioteca.comuna.dto.ComunaDTO;

@Component
public class ComunaModelAssembler implements RepresentationModelAssembler<ComunaDTO.Response, EntityModel<ComunaDTO.Response>>{

    @Override
    public EntityModel<ComunaDTO.Response> toModel(ComunaDTO.Response comuna) {
        // Retorna el DTO envuelto en un EntityModel junto con sus enlaces dinámicos
        return EntityModel.of(comuna,
                // Link "self": Enlace directo a este género específico basado en su ID
                linkTo(methodOn(ComunaControllerV2.class).buscarPorId(comuna.getId())).withSelfRel(),
                // Link "generos": Enlace de retorno hacia la colección completa v2
                linkTo(methodOn(ComunaControllerV2.class).listarTodos()).withRel("comunas")
        );
    }

}
