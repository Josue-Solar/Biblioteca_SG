package com.biblioteca.persona.controller;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.persona.assemblers.PersonaModelAssembler; // Asegúrate de que el paquete coincida con donde guardaste tu Assembler
import com.biblioteca.persona.dto.PersonaDTO;
import com.biblioteca.persona.dto.RolDTO;
import com.biblioteca.persona.dto.SexoDTO;
import com.biblioteca.persona.service.RolService;
import com.biblioteca.persona.service.SexoService;
import com.biblioteca.persona.service.impl.PersonaServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/personas")
@RequiredArgsConstructor
public class PersonaControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(PersonaControllerV2.class.getName());

    // Lombok generará automáticamente el constructor para estas dependencias finales
    private final PersonaServiceImpl personaService;
    private final SexoService sexoService;
    private final RolService rolService;
    private final PersonaModelAssembler assembler;

    // 1. Listar todos
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PersonaDTO.Response>> listarTodos() {
        logger.info("Recibiendo solicitud para listar personas (V2)");
        List<EntityModel<PersonaDTO.Response>> personas = personaService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(personas,
                linkTo(methodOn(PersonaControllerV2.class).listarTodos()).withSelfRel());
    }

    // 2. Guardar persona
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonaDTO.Response>> guardar(@Valid @RequestBody PersonaDTO.Request request) {
        logger.info("Recibiendo solicitud para guardar persona (V2)");
        PersonaDTO.Response response = personaService.save(request);
        return ResponseEntity
                .created(linkTo(methodOn(PersonaControllerV2.class).buscarPorId(response.getId())).toUri())
                .body(assembler.toModel(response));
    }

    // 3. Eliminar persona por ID
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        logger.info("Recibiendo solicitud para eliminar persona por ID: " + id + " (V2)");
        personaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // 4. Buscar por ID
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<PersonaDTO.Response> buscarPorId(@PathVariable("id") Long id) {
        logger.info("Recibiendo solicitud para buscar persona por ID: " + id + " (V2)");
        // Se asume que el servicio lanza excepción si no encuentra
        return assembler.toModel(personaService.findById(id));
    }

    // 5. Buscar por RUN
    @GetMapping(value = "/run/{run}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<PersonaDTO.Response> buscarPorRun(@PathVariable("run") String run) {
        logger.info("Recibiendo solicitud para buscar persona por RUN: " + run + " (V2)");
        return assembler.toModel(personaService.findByRun(run));
    }

    // 6. Buscar por ROL
    @GetMapping(value = "/rol/{rolId}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PersonaDTO.Response>> buscarPorRol(@PathVariable("rolId") Long rolId) {
        logger.info("Recibiendo solicitud para buscar persona por ROL: " + rolId + " (V2)");
        RolDTO.Response rol = rolService.findByIdOrThrow(rolId);
        List<EntityModel<PersonaDTO.Response>> personas = personaService.findByRol(rol).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(personas,
                linkTo(methodOn(PersonaControllerV2.class).buscarPorRol(rolId)).withSelfRel());
    }

    // 7. Buscar por Apellido
    @GetMapping(value = "/apellido/{apellido}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PersonaDTO.Response>> buscarPorApellido(@PathVariable("apellido") String apellido) {
        logger.info("Recibiendo solicitud para buscar persona por APELLIDO: " + apellido + " (V2)");
        List<EntityModel<PersonaDTO.Response>> personas = personaService.findByApPaterno(apellido).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(personas,
                linkTo(methodOn(PersonaControllerV2.class).buscarPorApellido(apellido)).withSelfRel());
    }

    // 8. Buscar por Sexo
    @GetMapping(value = "/sexo/{sexoId}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PersonaDTO.Response>> buscarPorSexo(@PathVariable("sexoId") Long sexoId) {
        logger.info("Recibiendo solicitud para buscar persona por SEXO: " + sexoId + " (V2)");
        SexoDTO.Response sexo = sexoService.findByIdOrThrow(sexoId);
        List<EntityModel<PersonaDTO.Response>> personas = personaService.findBySexo(sexo).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(personas,
                linkTo(methodOn(PersonaControllerV2.class).buscarPorSexo(sexoId)).withSelfRel());
    }

    // 9. Buscar por Comuna (Nombre)
    @GetMapping(value = "/comuna", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PersonaDTO.Response>> buscarPorComuna(@RequestParam("nombre") String nombre) {
        logger.info("Recibiendo solicitud para buscar persona por COMUNA (Nombre): " + nombre + " (V2)");
        // Adaptado del <?> del V1. Suponemos que findByComunaNombre retorna una lista válida.
        List<EntityModel<PersonaDTO.Response>> personas = personaService.findByComunaNombre(nombre).stream() // Cambia esto si devuelve un Object genérico
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(personas,
                linkTo(methodOn(PersonaControllerV2.class).buscarPorComuna(nombre)).withSelfRel());
    }

    // 10. Buscar por Comuna (ID)
    @GetMapping(value = "/comunaID/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<PersonaDTO.Response>> buscarPorComunaID(@PathVariable("id") Long id) {
        logger.info("Recibiendo solicitud para buscar persona por COMUNA (ID): " + id + " (V2)");
        List<EntityModel<PersonaDTO.Response>> personas = personaService.findByComunaID(id).stream() // Cambia esto si devuelve un Object genérico
                .map(assembler::toModel)
                .collect(Collectors.toList());
        
        return CollectionModel.of(personas,
                linkTo(methodOn(PersonaControllerV2.class).buscarPorComunaID(id)).withSelfRel());
    }

    // 11. Actualizar por ID
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<PersonaDTO.Response>> actualizar(@PathVariable("id") Long id, @Valid @RequestBody PersonaDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar persona por ID: " + id + " (V2)");
        PersonaDTO.Response personaActualizada = personaService.updatePersona(id, request);
        return ResponseEntity.ok(assembler.toModel(personaActualizada));
    }

}
