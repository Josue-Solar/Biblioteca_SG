package com.biblioteca.genero.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.genero.assemblers.GeneroModelAssembler;
import com.biblioteca.genero.dto.GeneroDTO;
import com.biblioteca.genero.dto.GeneroLibroDTO;
import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.service.GeneroService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/generos")
@RequiredArgsConstructor
@Tag(name = "Gestion de Generos V2 (HATEOAS)", description = "Operaciones de géneros literarios con soporte hipermedia (HAL+JSON)")
public class GeneroControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(GeneroControllerV2.class);

    private final GeneroService generoService;
    private final GeneroModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los géneros (HATEOAS)", description = "Retorna una colección hipermedia de todos los géneros")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colección de géneros obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No existen géneros registrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<Genero>>> listar() {
        logger.info("Recibiendo solicitud V2 para listar generos");
        List<Genero> generos = generoService.obtenerTodos();
        
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Mapeo inline para la entidad Genero pura
        List<EntityModel<Genero>> entityModels = generos.stream()
                .map(g -> EntityModel.of(g,
                        linkTo(methodOn(GeneroControllerV2.class).buscarPorId(g.getId())).withSelfRel(),
                        linkTo(methodOn(GeneroControllerV2.class).libroPorGenero(g.getId())).withRel("libros-asociados")))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(GeneroControllerV2.class).listar()).withSelfRel()));
    }

    @GetMapping(value = "/nombre/{nombre}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar géneros por nombre (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<GeneroDTO.Response>>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud V2 para buscar genero por nombre: {}", nombre);
        List<GeneroDTO.Response> generos = generoService.obtenerPorNombre(nombre);
        
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<GeneroDTO.Response>> entityModels = generos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(GeneroControllerV2.class).buscarPorNombre(nombre)).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar género por ID (HATEOAS)")
    public ResponseEntity<EntityModel<GeneroDTO.Response>> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud V2 para buscar genero por ID: {}", id);
        try {
            GeneroDTO.Response genero = generoService.findByIdOrThrow(id);
            return ResponseEntity.ok(assembler.toModel(genero));
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{generoId}/libros", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros de un género (HATEOAS)", description = "Retorna el género junto con la colección de libros que le pertenecen")
    public ResponseEntity<EntityModel<GeneroLibroDTO>> libroPorGenero(@PathVariable long generoId) {
        logger.info("Recibiendo solicitud V2 para buscar libros por el genero ID: {}", generoId);
        try {
            GeneroLibroDTO generoLibroDTO = generoService.librosPorGenero(generoId);
            
            // Mapeo inline para el DTO combinado
            EntityModel<GeneroLibroDTO> entityModel = EntityModel.of(generoLibroDTO,
                    linkTo(methodOn(GeneroControllerV2.class).libroPorGenero(generoId)).withSelfRel(),
                    linkTo(methodOn(GeneroControllerV2.class).buscarPorId(generoId)).withRel("genero-detalle"),
                    linkTo(methodOn(GeneroControllerV2.class).listar()).withRel("generos"));
                    
            return ResponseEntity.ok(entityModel);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo género (HATEOAS)")
    public ResponseEntity<EntityModel<GeneroDTO.Response>> crear(@Valid @RequestBody GeneroDTO.Request genero) {
        logger.info("Recibiendo solicitud V2 para guardar nuevo genero");
        GeneroDTO.Response nuevoGenero = generoService.guardar(genero);
        
        EntityModel<GeneroDTO.Response> entityModel = assembler.toModel(nuevoGenero);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un género (HATEOAS)")
    public ResponseEntity<EntityModel<GeneroDTO.Response>> actualizar(@PathVariable long id, @Valid @RequestBody GeneroDTO.Request genero) {
        logger.info("Recibiendo solicitud V2 para actualizar genero ID: {}", id);
        GeneroDTO.Response generoActualizado = generoService.modificarGenero(id, genero);  
        
        if (generoActualizado != null) {
            return ResponseEntity.ok(assembler.toModel(generoActualizado));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un género (HATEOAS)")
    public ResponseEntity<Void> eliminarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud V2 para eliminar genero por ID: {}", id);
        try {
            generoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al intentar eliminar genero por ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
