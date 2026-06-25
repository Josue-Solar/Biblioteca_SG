package com.biblioteca.reserva.assemblers;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.biblioteca.reserva.controller.ReservaControllerV2;
import com.biblioteca.reserva.dto.ReservaDTO;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservaModelAssembler implements RepresentationModelAssembler<ReservaDTO.Response, EntityModel<ReservaDTO.Response>> {

    @Override
    public EntityModel<ReservaDTO.Response> toModel(ReservaDTO.Response reserva) {
        return EntityModel.of(reserva,
            linkTo(methodOn(ReservaControllerV2.class).buscarPorId(reserva.getId())).withSelfRel(),
            linkTo(methodOn(ReservaControllerV2.class).listar()).withRel("reservas"),
            linkTo(methodOn(ReservaControllerV2.class).buscarPorPersona(reserva.getPersonaId())).withRel("reservas-por-persona"),
            linkTo(methodOn(ReservaControllerV2.class).buscarPorEjemplar(reserva.getEjemplarId())).withRel("reservas-por-ejemplar")
        );
    }
}