package com.biblioteca.ejemplar.controller;

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

import com.biblioteca.ejemplar.assemblers.EjemplarModelAssembler;
import com.biblioteca.ejemplar.dto.EjemplarDTO.Response;
import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.service.EjemplarService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/ejemplares")
@RequiredArgsConstructor
@Tag(name = "Gestion de Ejemplares V2 (HATEOAS)", description = "Operaciones hipermedia para la gestión de ejemplares físicos")
public class EjemplarControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(EjemplarControllerV2.class);

    private final EjemplarService ejemplarService;
    private final EjemplarModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los ejemplares (HATEOAS)", description = "Retorna una colección hipermedia de todos los ejemplares físicos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colección de ejemplares obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No existen ejemplares en el sistema")
    })
    public ResponseEntity<CollectionModel<EntityModel<Ejemplar>>> listar() {
        logger.info("Recibiendo solicitud V2 para listar todos los ejemplares");
        List<Ejemplar> ejemplares = ejemplarService.obtenerTodos();

        if (ejemplares.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<Ejemplar>> entityModels = ejemplares.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Ejemplar>> collectionModel = CollectionModel.of(entityModels,
                linkTo(methodOn(EjemplarControllerV2.class).listar()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar ejemplar por ID (HATEOAS)", description = "Busca un ejemplar específico y genera sus enlaces de navegación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar localizado correctamente"),
        @ApiResponse(responseCode = "404", description = "El ejemplar con el ID suministrado no existe")
    })
    public ResponseEntity<EntityModel<Ejemplar>> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para buscar ejemplar por ID: {}", id);
        return ejemplarService.obtenerPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear un nuevo ejemplar (HATEOAS)", description = "Registra un ejemplar físico y devuelve el recurso con sus enlaces")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ejemplar creado de forma exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    public ResponseEntity<EntityModel<Ejemplar>> guardar(@Valid @RequestBody Ejemplar ejemplar) {
        logger.info("Recibiendo solicitud V2 para guardar un nuevo ejemplar");
        Ejemplar nuevoEjemplar = ejemplarService.guardar(ejemplar);
        EntityModel<Ejemplar> entityModel = assembler.toModel(nuevoEjemplar);
        
        return ResponseEntity
                .created(entityModel.getRequiredLink("self").toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un ejemplar (HATEOAS)", description = "Modifica los datos de un ejemplar existente y retorna el recurso actualizado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "El ejemplar solicitado no existe")
    })
    public ResponseEntity<EntityModel<Ejemplar>> actualizar(@PathVariable Long id, @Valid @RequestBody Ejemplar ejemplar) {
        logger.info("Recibiendo solicitud V2 para actualizar el ejemplar ID: {}", id);
        // Ajustado al método de tu service
        Object resultado = ejemplarService.modReserva(id, ejemplar); 
        
        // El service en V1 retorna un Object/Ejemplar, hacemos el cast seguro si es necesario
        Ejemplar ejemplarActualizado = (Ejemplar) resultado;
        return ResponseEntity.ok(assembler.toModel(ejemplarActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Eliminar un ejemplar (HATEOAS)", description = "Remueve permanentemente un ejemplar del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ejemplar eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el ejemplar a eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para eliminar el ejemplar ID: {}", id);
        ejemplarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints de búsqueda especializada adaptados a HATEOAS ---

    @GetMapping(value = "/isbn/{isbn}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar ejemplares por ISBN (HATEOAS)", description = "Retorna la colección de ejemplares que pertenecen a un ISBN")
    public ResponseEntity<CollectionModel<EntityModel<Ejemplar>>> buscarPorIsbn(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud V2 para buscar ejemplares por ISBN: {}", isbn);
        List<Ejemplar> ejemplares = (List<Ejemplar>) ejemplarService.obtenerTodosPorIsbn(isbn);
        
        List<EntityModel<Ejemplar>> entityModels = ejemplares.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(EjemplarControllerV2.class).buscarPorIsbn(isbn)).withSelfRel()));
    }

    @GetMapping(value = "/edicion/{edicionId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar ejemplares por Edición (HATEOAS)", description = "Retorna la colección de ejemplares de una edición en específico")
    public ResponseEntity<CollectionModel<EntityModel<Ejemplar>>> buscarPorEdicion(@PathVariable Long edicionId) {
        logger.info("Recibiendo solicitud V2 para buscar ejemplares por Edición ID: {}", edicionId);
        List<Ejemplar> ejemplares = ejemplarService.obtenerTodosPorEdicionId(edicionId);
        
        List<EntityModel<Ejemplar>> entityModels = ejemplares.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(EjemplarControllerV2.class).buscarPorEdicion(edicionId)).withSelfRel()));
    }
}
