package com.example.edicion.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.edicion.assemblers.EdicionModelAssembler;
import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.service.EdicionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/ediciones")
@RequiredArgsConstructor
@Tag(name = "Gestion de Ediciones V2", description = "Operaciones con HATEOAS relacionadas con las ediciones de libros")
public class EdicionControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(EdicionControllerV2.class);

    private final EdicionService edicionService;
    private final EdicionModelAssembler assembler;

    // 1. Obtener todas las ediciones
    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las ediciones (HATEOAS)", description = "Obtiene una lista de todas las ediciones con enlaces HATEOAS")
    public CollectionModel<EntityModel<EdicionDTO.Response>> listarTodas() {
        logger.info("Recibiendo solicitud para listar todas las ediciones V2");
        
        List<EntityModel<EdicionDTO.Response>> ediciones = edicionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ediciones,
                linkTo(methodOn(EdicionControllerV2.class).listarTodas()).withSelfRel());
    }

    // 2. Buscar por ID (Ruta limpia sin /id:)
    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar edición por ID (HATEOAS)", description = "Obtiene los detalles de una edición específica mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Edición encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró la edición con el ID proporcionado")
    })
    public EntityModel<EdicionDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar edición por ID V2: {}", id);
        return assembler.toModel(edicionService.obtenerPorId(id)); // Nota: En tu V1 se llamaba obtenerPorId
    }

    // 3. Obtener ediciones por Editorial (Consolidado y limpio)
    @GetMapping(value = "/editorial/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener ediciones por Editorial (HATEOAS)", description = "Obtiene una lista de ediciones asociadas a una editorial específica")
    public CollectionModel<EntityModel<EdicionDTO.Response>> listarPorEditorial(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para listar ediciones por editorial ID V2: {}", id);
        
        List<EntityModel<EdicionDTO.Response>> ediciones = edicionService.listarEdiciones(id).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(ediciones,
                linkTo(methodOn(EdicionControllerV2.class).listarPorEditorial(id)).withSelfRel());
    }

    // 4. Obtener libros por Edición
    @GetMapping(value = "/{edicionId}/libros", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener libros por edición V2", description = "Obtiene los libros que pertenecen a una edición en específico")
    public ResponseEntity<?> librosPorEdicion(@PathVariable Long edicionId) {
        logger.info("Recibiendo solicitud para obtener libros de la edición ID V2: {}", edicionId);
        return ResponseEntity.ok(edicionService.librosPorEdicion(edicionId));
    }

    // 5. Crear Edición
    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva edición (HATEOAS)", description = "Registra una edición y retorna el recurso con sus hipermedios")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Edición creada de manera exitosa")
    })
    public ResponseEntity<EntityModel<EdicionDTO.Response>> guardar(@Valid @RequestBody EdicionDTO.Request request) {
        logger.info("Recibiendo solicitud para guardar una nueva edición V2");
        
        EdicionDTO.Response response = edicionService.guardar(request);
        
        return ResponseEntity
                .created(linkTo(methodOn(EdicionControllerV2.class).buscarPorId(response.getId())).toUri())
                .body(assembler.toModel(response));
    }

    // 6. Actualizar Edición (Ruta limpia sin /editar:)
    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una edición (HATEOAS)", description = "Modifica los datos de una edición existente mediante su ID")
    public ResponseEntity<EntityModel<EdicionDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody EdicionDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar edición ID V2: {}", id);
        return ResponseEntity.ok(assembler.toModel(edicionService.actualizar(id, request)));
    }

    // 7. Eliminar Edición (Ruta limpia sin /eliminar:)
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Eliminar una edición por ID V2", description = "Remueve la edición del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar edición ID V2: {}", id);
        edicionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
