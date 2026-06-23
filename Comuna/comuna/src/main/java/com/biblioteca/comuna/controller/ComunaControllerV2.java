package com.biblioteca.comuna.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.comuna.assemblers.ComunaModelAssembler;
import com.biblioteca.comuna.dto.ComunaDTO;
import com.biblioteca.comuna.service.ComunaService;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/comunas")
@RequiredArgsConstructor
public class ComunaControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(ComunaController.class.getName());

    private final ComunaService comunaService;
    
    private final ComunaModelAssembler assembler;

    //probando v2 hateoas
    /*public ResponseEntity<List<ComunaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar comunas");//log
        List<ComunaDTO.Response> comunas = comunaService.findAll();
        if(comunas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }*/

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public CollectionModel<EntityModel<ComunaDTO.Response>> listarTodos() {
        logger.info("Recibiendo solicitud para listar comunas");//log
        // Obtiene la lista simple desde el servicio v1
        List<EntityModel<ComunaDTO.Response>> comunas = comunaService.findAll().stream()
                .map(assembler::toModel) // Transforma cada elemento añadiéndole enlaces individuales
                .collect(Collectors.toList());
        // Retorna la colección envuelta junto con el enlace self del listado completo
        return CollectionModel.of(comunas,
                linkTo(methodOn(ComunaControllerV2.class).listarTodos()).withSelfRel());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO.Response>> guardar(@Valid @RequestBody ComunaDTO.Request request) {
        logger.info("Recibiendo solicitud para guardar comuna");//log
        ComunaDTO.Response response = comunaService.save(request);
        return ResponseEntity
                .created(linkTo(methodOn(ComunaControllerV2.class).buscarPorId(response.getId())).toUri())
                .body(assembler.toModel(response));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para borrar comuna por id");//log
        comunaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por ID
    /*@GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ComunaDTO.Response> buscarPorId(@PathVariable Long id) {
        ComunaDTO.Response comuna = comunaService.findByIdOrThrow(id);
        return assembler.toModel(comuna);
    }*/
    //buscar por id, metodo moderno
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public EntityModel<ComunaDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar comuna por id");//log
        return assembler.toModel(comunaService.findByIdOrThrow(id));
    }

    // buscar por nombre, no se ocupa en hateoas

    //actualizar por id
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<ComunaDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody ComunaDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Comuna por ID: " + id);
        return ResponseEntity.ok(assembler.toModel(comunaService.update(id, request)));
    }


}
