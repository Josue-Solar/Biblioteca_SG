package com.biblioteca.editorial.controller;

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

import com.biblioteca.editorial.assemblers.EditorialModelAssembler;
import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.service.EditorialService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/editoriales")
@RequiredArgsConstructor
@Tag(name = "Gestion de Editoriales V2 (HATEOAS)", description = "Operaciones orientadas a hipermedios para el recurso Editorial")
public class EditorialControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(EditorialControllerV2.class.getName());

    private final EditorialService editorialService;
    private final EditorialModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las editoriales (HATEOAS)", description = "Retorna una colección hipermedia de todas las editoriales registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colección de editoriales recuperada con éxito"),
        @ApiResponse(responseCode = "204", description = "No existen registros de editoriales")
    })
    public ResponseEntity<CollectionModel<EntityModel<EditorialDTO.Response>>> listar() {
        logger.info("Recibiendo solicitud V2 para listar editoriales con HATEOAS");
        List<EditorialDTO.Response> editoriales = editorialService.findAll();

        if (editoriales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<EditorialDTO.Response>> entityModels = editoriales.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EditorialDTO.Response>> collectionModel = CollectionModel.of(entityModels,
                linkTo(methodOn(EditorialControllerV2.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva editorial (HATEOAS)", description = "Registra una editorial y retorna su representación con enlaces HATEOAS")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Editorial creada de forma exitosa"),
        @ApiResponse(responseCode = "400", description = "Petición incorrecta o error de validación en los campos")
    })
    public ResponseEntity<EntityModel<EditorialDTO.Response>> guardar(@Valid @RequestBody EditorialDTO.Request request) {
        logger.info("Recibiendo solicitud V2 para guardar una nueva editorial");
        EditorialDTO.Response response = editorialService.save(request);
        
        EntityModel<EditorialDTO.Response> entityModel = assembler.toModel(response);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar una editorial por ID (HATEOAS)", description = "Busca una editorial específica y genera sus respectivos enlaces hipermedia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Editorial localizada correctamente"),
        @ApiResponse(responseCode = "404", description = "El ID proporcionado no pertenece a ninguna editorial")
    })
    public EntityModel<EditorialDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para buscar editorial por id");
        return assembler.toModel(editorialService.findByIdOrThrow(id));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una editorial por ID (HATEOAS)", description = "Modifica los datos de una editorial existente y devuelve el recurso actualizado con enlaces")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Editorial actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "La editorial solicitada no existe")
    })
    public ResponseEntity<EntityModel<EditorialDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody EditorialDTO.Request request) {
        logger.info("Recibiendo solicitud V2 para actualizar Editorial por ID: " + id);
        EditorialDTO.Response response = editorialService.update(id, request);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar una editorial (HATEOAS)", description = "Elimina de forma permanente una editorial según su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Editorial removida con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró la editorial con el ID suministrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para borrar editorial por id");
        editorialService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
