package com.biblioteca.reserva.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import com.biblioteca.reserva.dto.ReservaDTO;
import com.biblioteca.reserva.service.ReservaService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor // Reemplaza a @Autowired (Mejor práctica)
@Tag(name = "Gestion de Reservas", description = "Operaciones relacionadas con las reservas de ejemplares de la biblioteca")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class);

    private final ReservaService reservaService; // Ahora es 'final'

    // CRUD BÁSICO
    @GetMapping
    @Operation(summary = "Obtener todas las reservas", description = "Obtiene una lista general de todas las reservas registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reservas obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay reservas registradas en el sistema")
    })
    public ResponseEntity<List<ReservaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar reservas");
        List<ReservaDTO.Response> reservas = reservaService.findAll();
        if(reservas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reserva", description = "Registra una solicitud de reserva en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reserva creada de forma exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación o regla de negocio en los datos enviados")
    })
    public ResponseEntity<ReservaDTO.Response> guardar(@Valid @RequestBody ReservaDTO.Request request){
        logger.info("Recibiendo solicitud para guardar reserva");
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar reserva por ID", description = "Obtiene los detalles específicos de una reserva mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró la reserva con el ID proporcionado")
    })
    public ResponseEntity<ReservaDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar reserva por ID: {}", id);
        return ResponseEntity.ok(reservaService.findByIdOrThrow(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reserva", description = "Modifica los datos de una reserva existente utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "La reserva que se intenta actualizar no existe")
    })
    public ResponseEntity<ReservaDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody ReservaDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar reserva con ID: {}", id);
        return ResponseEntity.ok(reservaService.updateReserva(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reserva", description = "Remueve permanentemente una reserva del sistema mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Reserva eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró la reserva a eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar reserva por ID: {}", id);
        reservaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // BÚSQUEDAS ESPECÍFICAS
    @GetMapping("/persona/{personaId}")
    @Operation(summary = "Buscar reservas por Persona", description = "Busca y devuelve todas las reservas asociadas al ID de un usuario/persona")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas obtenidas con éxito")
    })
    public ResponseEntity<List<ReservaDTO.Response>> buscarPorPersona(@PathVariable Long personaId) {
        logger.info("Recibiendo solicitud para buscar reservas por el ID de la PERSONA: {}", personaId);
        return ResponseEntity.ok(reservaService.findByPersonaId(personaId));
    }

    @GetMapping("/ejemplar/{ejemplarId}")
    @Operation(summary = "Buscar reservas por Ejemplar", description = "Busca y devuelve las reservas asociadas al ID de un ejemplar físico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reservas obtenidas con éxito")
    })
    public ResponseEntity<List<ReservaDTO.Response>> buscarPorEjemplar(@PathVariable Long ejemplarId) {
        logger.info("Recibiendo solicitud para buscar reservas por el ID del EJEMPLAR: {}", ejemplarId);
        return ResponseEntity.ok(reservaService.findByEjemplarId(ejemplarId));
    }

    @GetMapping("/activas")
    @Operation(summary = "Listar reservas activas", description = "Obtiene una lista de todas las reservas que se encuentran actualmente activas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de reservas activas obtenida con éxito")
    })
    public ResponseEntity<List<ReservaDTO.Response>> buscarActivas() {
        logger.info("Recibiendo solicitud para listar reservas ACTIVAS");
        return ResponseEntity.ok(reservaService.findReservasActivas());
    }

    // REGLAS DE NEGOCIO
    @PutMapping("/{id}/retiro")
    @Operation(summary = "Registrar retiro de reserva", description = "Cambia el estado de una reserva cuando el usuario retira físicamente el ejemplar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retiro registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "La reserva no está en un estado válido para retiro"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDTO.Response> registrarRetiro(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para registrar el retiro de la reserva ID: {}", id);
        return ResponseEntity.ok(reservaService.registrarRetiro(id));
    }

    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar una reserva", description = "Permite cancelar una reserva activa por parte del usuario o la biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reserva cancelada exitosamente"),
        @ApiResponse(responseCode = "400", description = "La reserva no puede ser cancelada en su estado actual"),
        @ApiResponse(responseCode = "404", description = "Reserva no encontrada")
    })
    public ResponseEntity<ReservaDTO.Response> cancelarReserva(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para cancelar la reserva ID: {}", id);
        return ResponseEntity.ok(reservaService.cancelarReserva(id));
    }

    @PostMapping("/procesar-expiradas")
    @Operation(summary = "Procesar reservas expiradas", description = "Ejecuta manualmente el proceso de limpieza y liberación de reservas que han caducado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Proceso de liberación de reservas expiradas ejecutado correctamente")
    })
    public ResponseEntity<Void> procesarExpiradas() {
        logger.info("Recibiendo solicitud manual para procesar y limpiar reservas expiradas");
        reservaService.procesarReservasExpiradas();
        return ResponseEntity.ok().build(); 
    }
}