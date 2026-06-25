package com.biblioteca.prestamo.assemblers;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.biblioteca.prestamo.controller.PrestamoControllerV2;
import com.biblioteca.prestamo.dto.PrestamoDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrestamoModelAssembler implements RepresentationModelAssembler<PrestamoDTO.Response, EntityModel<PrestamoDTO.Response>> {

    @Override
    public EntityModel<PrestamoDTO.Response> toModel(PrestamoDTO.Response prestamo) {
        return EntityModel.of(prestamo,
                // Enlace hacia el detalle del préstamo
                linkTo(methodOn(PrestamoControllerV2.class).buscarPorId(prestamo.getId())).withSelfRel(),
                
                // Enlace para volver a la lista general
                linkTo(methodOn(PrestamoControllerV2.class).listar()).withRel("prestamos"),
                
                // Enlace de acción rápida para registrar devolución
                linkTo(methodOn(PrestamoControllerV2.class).registrarDevolucion(prestamo.getId())).withRel("registrar-devolucion")
        );
    }
}