package com.biblioteca.prestamo.controller;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.prestamo.assemblers.PrestamoModelAssembler;
import com.biblioteca.prestamo.dto.PrestamoDTO;
import com.biblioteca.prestamo.service.PrestamoService;

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
@RequestMapping("/api/v2/prestamos")
@RequiredArgsConstructor
@Tag(name = "Gestion de Prestamos V2 (HATEOAS)", description = "Operaciones de préstamos con navegación interactiva a través de hipermedios (HAL+JSON)")
public class PrestamoControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoControllerV2.class);

    private final PrestamoService prestamoService;
    private final PrestamoModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los préstamos (HATEOAS)", description = "Retorna una colección hipermedia de todos los préstamos registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colección de préstamos obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay préstamos registrados en el sistema")
    })
    public ResponseEntity<CollectionModel<EntityModel<PrestamoDTO.Response>>> listar() {
        logger.info("Recibiendo solicitud V2 para listar prestamos");
        List<PrestamoDTO.Response> prestamos = prestamoService.findAll();
        
        if (prestamos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<PrestamoDTO.Response>> entityModels = prestamos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(PrestamoControllerV2.class).listar()).withSelfRel()));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar un nuevo préstamo (HATEOAS)", description = "Crea un nuevo registro de préstamo y retorna el recurso hipermedia generado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación de negocio (Ej: Ejemplar no disponible)")
    })
    public ResponseEntity<EntityModel<PrestamoDTO.Response>> guardar(@Valid @RequestBody PrestamoDTO.Request request) {
        logger.info("Recibiendo solicitud V2 para guardar un nuevo prestamo");
        PrestamoDTO.Response response = prestamoService.crear(request);
        
        EntityModel<PrestamoDTO.Response> entityModel = assembler.toModel(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar préstamo por ID (HATEOAS)")
    public ResponseEntity<EntityModel<PrestamoDTO.Response>> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para buscar prestamo por ID: {}", id);
        PrestamoDTO.Response response = prestamoService.findByIdOrThrow(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @GetMapping(value = "/persona/{personaId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar préstamos por Persona (HATEOAS)", description = "Devuelve la colección hipermedia de préstamos asociados a una persona específica")
    public ResponseEntity<CollectionModel<EntityModel<PrestamoDTO.Response>>> buscarPorPersona(@PathVariable Long personaId) {
        logger.info("Recibiendo solicitud V2 para buscar prestamos por el ID de la PERSONA: {}", personaId);
        List<PrestamoDTO.Response> prestamos = prestamoService.findByPersonaId(personaId);
        
        List<EntityModel<PrestamoDTO.Response>> entityModels = prestamos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(PrestamoControllerV2.class).buscarPorPersona(personaId)).withSelfRel()));
    }

    @GetMapping(value = "/atrasados", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar préstamos atrasados (HATEOAS)")
    public ResponseEntity<CollectionModel<EntityModel<PrestamoDTO.Response>>> buscarPrestamosAtrasados() {
        logger.info("Recibiendo solicitud V2 para buscar prestamos atrasados");
        List<PrestamoDTO.Response> prestamos = prestamoService.findPrestamosAtrasados();
        
        List<EntityModel<PrestamoDTO.Response>> entityModels = prestamos.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(PrestamoControllerV2.class).buscarPrestamosAtrasados()).withSelfRel()));
    }

    @PatchMapping(value = "/{id}/devolver", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar devolución de un préstamo (HATEOAS)", description = "Procesa la devolución y entrega el estado actualizado del préstamo con sus enlaces")
    public ResponseEntity<EntityModel<PrestamoDTO.Response>> registrarDevolucion(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para registrar la devolucion del prestamo ID: {}", id);
        PrestamoDTO.Response response = prestamoService.registrarDevolucion(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un préstamo (HATEOAS)")
    public ResponseEntity<EntityModel<PrestamoDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody PrestamoDTO.Request request) {
        logger.info("Recibiendo solicitud V2 para actualizar prestamo con ID: {}", id);
        PrestamoDTO.Response response = prestamoService.updatePrestamo(id, request);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un préstamo")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para borrar prestamo por ID: {}", id);
        prestamoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
