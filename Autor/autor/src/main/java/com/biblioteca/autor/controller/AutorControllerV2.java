package com.biblioteca.autor.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.autor.assemblers.AutorModelAssembler;
import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.service.AutorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/autores")
@RequiredArgsConstructor
@Tag(name = "Gestion de Autores V2", description = "Operaciones con HATEOAS relacionadas con los autores")
public class AutorControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(AutorControllerV2.class);
    
    private final AutorService autorService;
    private final AutorModelAssembler assembler;

    // Obtener todos
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los autores (HATEOAS)", description = "Obtiene una lista de todos los autores con enlaces HATEOAS")
    public CollectionModel<EntityModel<AutorDTO.Response>> listarTodos() {
        logger.info("Recibiendo solicitud para listar autores V2");
        
        List<EntityModel<AutorDTO.Response>> autores = autorService.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(autores,
                linkTo(methodOn(AutorControllerV2.class).listarTodos()).withSelfRel());
    }

    // Crear
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo autor (HATEOAS)", description = "Registra un nuevo autor y devuelve el recurso creado con sus enlaces")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Autor creado exitosamente")
    })
    public ResponseEntity<EntityModel<AutorDTO.Response>> guardar(@Valid @RequestBody AutorDTO.Request request) {
        logger.info("Recibiendo solicitud para guardar autor V2");
        
        AutorDTO.Response response = autorService.save(request);
        
        return ResponseEntity
                .created(linkTo(methodOn(AutorControllerV2.class).buscarPorId(response.getId())).toUri())
                .body(assembler.toModel(response));
    }

    // Borrar
    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un autor por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar autor V2");
        autorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por ID
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar autor por ID (HATEOAS)")
    public EntityModel<AutorDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar autor por ID V2");
        return assembler.toModel(autorService.findByIdOrThrow(id));
    }

    // Buscar por Apellido (Devuelve una colección HATEOAS)
    @GetMapping(value = "/apellido/{apellido}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar autor por apellido (HATEOAS)")
    public CollectionModel<EntityModel<AutorDTO.Response>> buscarPorApellido(@PathVariable String apellido) {
        logger.info("Recibiendo solicitud para buscar autor por apellido V2");
        
        List<EntityModel<AutorDTO.Response>> autores = autorService.findByApPaterno(apellido).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(autores,
                linkTo(methodOn(AutorControllerV2.class).buscarPorApellido(apellido)).withSelfRel());
    }

    // Buscar por Nombre (Devuelve una colección HATEOAS)
    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar autor por nombre (HATEOAS)")
    public CollectionModel<EntityModel<AutorDTO.Response>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar autor por nombre V2");
        
        List<EntityModel<AutorDTO.Response>> autores = autorService.findByPrimerNombre(nombre).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(autores,
                linkTo(methodOn(AutorControllerV2.class).buscarPorNombre(nombre)).withSelfRel());
    }

    // Actualizar
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un autor (HATEOAS)")
    public ResponseEntity<EntityModel<AutorDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Autor por ID V2: " + id);
        return ResponseEntity.ok(assembler.toModel(autorService.updateAutor(id, request)));
    }
}

