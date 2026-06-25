package com.biblioteca.reserva.controller;

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

import com.biblioteca.reserva.assemblers.ReservaModelAssembler;
import com.biblioteca.reserva.dto.ReservaDTO;
import com.biblioteca.reserva.service.ReservaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v2/reservas")
@RequiredArgsConstructor
@Tag(name = "Gestion de Reservas V2 (HATEOAS)", description = "Operaciones del ciclo de vida de reservas soportando navegación Hipermedia (HAL+JSON)")
public class ReservaControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(ReservaControllerV2.class);

    private final ReservaService reservaService;
    private final ReservaModelAssembler assembler;

    // ==========================================
    //               CRUD BÁSICO
    // ==========================================

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todas las reservas (HATEOAS)", description = "Retorna una colección de todas las reservas con sus respectivos enlaces hipermedia.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colección de reservas obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No existen reservas en el sistema")
    })
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO.Response>>> listar() {
        logger.info("Recibiendo solicitud V2 para listar reservas");
        List<ReservaDTO.Response> reservas = reservaService.findAll();
        
        if (reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<ReservaDTO.Response>> entityModels = reservas.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(ReservaControllerV2.class).listar()).withSelfRel()));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar reserva por ID (HATEOAS)", description = "Retorna los detalles de una reserva en formato HAL+JSON mediante su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<EntityModel<ReservaDTO.Response>> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para buscar reserva por ID: {}", id);
        ReservaDTO.Response response = reservaService.findByIdOrThrow(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Crear una nueva reserva (HATEOAS)", description = "Registra una reserva y retorna el recurso creado adjuntando sus enlaces de acción.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada de forma exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación o regla de negocio en los datos enviados")
    })
    public ResponseEntity<EntityModel<ReservaDTO.Response>> guardar(@Valid @RequestBody ReservaDTO.Request request) {
        logger.info("Recibiendo solicitud V2 para registrar una reserva");
        ReservaDTO.Response response = reservaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(response));
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar una reserva (HATEOAS)", description = "Modifica los datos de una reserva existente y retorna el recurso actualizado con enlaces actualizados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "La reserva solicitada no existe")
    })
    public ResponseEntity<EntityModel<ReservaDTO.Response>> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO.Request request) {
        logger.info("Recibiendo solicitud V2 para actualizar reserva con ID: {}", id);
        ReservaDTO.Response response = reservaService.updateReserva(id, request);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Remueve permanentemente la reserva identificada por el ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró la reserva a eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para borrar reserva por ID: {}", id);
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    //           BÚSQUEDAS ESPECÍFICAS
    // ==========================================

    @GetMapping(value = "/persona/{personaId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar reservas por Persona (HATEOAS)", description = "Obtiene la colección de todas las reservas que pertenecen a una persona específica.")
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO.Response>>> buscarPorPersona(@PathVariable Long personaId) {
        logger.info("Recibiendo solicitud V2 para buscar reservas de la persona: {}", personaId);
        List<EntityModel<ReservaDTO.Response>> entityModels = reservaService.findByPersonaId(personaId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(ReservaControllerV2.class).buscarPorPersona(personaId)).withSelfRel()));
    }

    @GetMapping(value = "/ejemplar/{ejemplarId}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar reservas por Ejemplar (HATEOAS)", description = "Obtiene el historial o colección de reservas asociadas a un ejemplar de libro físico.")
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO.Response>>> buscarPorEjemplar(@PathVariable Long ejemplarId) {
        logger.info("Recibiendo solicitud V2 para buscar reservas del ejemplar: {}", ejemplarId);
        List<EntityModel<ReservaDTO.Response>> entityModels = reservaService.findByEjemplarId(ejemplarId).stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(ReservaControllerV2.class).buscarPorEjemplar(ejemplarId)).withSelfRel()));
    }

    @GetMapping(value = "/activas", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar reservas activas (HATEOAS)", description = "Devuelve exclusivamente la colección hipermedia de reservas que están vigentes.")
    public ResponseEntity<CollectionModel<EntityModel<ReservaDTO.Response>>> buscarActivas() {
        logger.info("Recibiendo solicitud V2 para listar reservas ACTIVAS");
        List<EntityModel<ReservaDTO.Response>> entityModels = reservaService.findReservasActivas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(ReservaControllerV2.class).buscarActivas()).withSelfRel()));
    }

    // ==========================================
    //             REGLAS DE NEGOCIO
    // ==========================================

    @PutMapping(value = "/{id}/retiro", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar retiro físico de libro (HATEOAS)", description = "Transiciona el estado de la reserva al confirmarse el retiro del libro.")
    public ResponseEntity<EntityModel<ReservaDTO.Response>> registrarRetiro(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para registrar retiro físico de la reserva ID: {}", id);
        ReservaDTO.Response response = reservaService.registrarRetiro(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @PutMapping(value = "/{id}/cancelar", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Cancelar reserva (HATEOAS)", description = "Cancela la reserva seleccionada liberando inmediatamente el ejemplar para el sistema.")
    public ResponseEntity<EntityModel<ReservaDTO.Response>> cancelarReserva(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para cancelar la reserva ID: {}", id);
        ReservaDTO.Response response = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(assembler.toModel(response));
    }

    @PostMapping("/procesar-expiradas")
    @Operation(summary = "Procesar reservas expiradas de forma manual", description = "Ejecuta de manera asíncrona o directa el barrido de reservas cuyo tiempo límite caducó.")
    public ResponseEntity<Void> procesarExpiradas() {
        logger.info("Recibiendo solicitud V2 manual para limpieza de reservas expiradas");
        reservaService.procesarReservasExpiradas();
        return ResponseEntity.ok().build(); 
    }
}