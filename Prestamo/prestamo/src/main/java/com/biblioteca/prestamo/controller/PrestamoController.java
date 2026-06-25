package com.biblioteca.prestamo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import com.biblioteca.prestamo.dto.PrestamoDTO;
import com.biblioteca.prestamo.service.PrestamoService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/prestamos")
@RequiredArgsConstructor // Reemplaza a @Autowired (Mejor práctica)
@Tag(name = "Gestion de Prestamos", description = "Operaciones relacionadas con los préstamos de libros a usuarios")
public class PrestamoController {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoController.class);

    private final PrestamoService prestamoService; // Ahora es 'final'

    @GetMapping
    @Operation(summary = "Obtener todos los préstamos", description = "Obtiene una lista general de todos los préstamos registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay préstamos registrados en el sistema")
    })
    public ResponseEntity<List<PrestamoDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar prestamos");
        List<PrestamoDTO.Response> prestamos = prestamoService.findAll();
        if(prestamos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(prestamos);
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo préstamo", description = "Crea un nuevo registro de préstamo validando disponibilidad del ejemplar y reglas de negocio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Préstamo creado de forma exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación o regla de negocio en los datos enviados")
    })
    public ResponseEntity<PrestamoDTO.Response> guardar(@Valid @RequestBody PrestamoDTO.Request request){
        logger.info("Recibiendo solicitud para guardar un nuevo prestamo");
        return ResponseEntity.status(HttpStatus.CREATED).body(prestamoService.crear(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar préstamo por ID", description = "Obtiene los detalles específicos de un préstamo mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Préstamo encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró el préstamo con el ID proporcionado")
    })
    public ResponseEntity<PrestamoDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar prestamo por ID: {}", id);
        return ResponseEntity.ok(prestamoService.findByIdOrThrow(id));
    }

    @GetMapping("/persona/{personaId}")
    @Operation(summary = "Buscar préstamos por Persona", description = "Obtiene la lista de todos los préstamos asociados al ID de un usuario/persona")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de préstamos obtenida con éxito")
    })
    public ResponseEntity<List<PrestamoDTO.Response>> buscarPorPersona(@PathVariable Long personaId) {
        logger.info("Recibiendo solicitud para buscar prestamos por el ID de la PERSONA: {}", personaId);
        return ResponseEntity.ok(prestamoService.findByPersonaId(personaId));
    }

    @GetMapping("/atrasados")
    @Operation(summary = "Listar préstamos atrasados", description = "Devuelve una lista con todos los préstamos cuya fecha límite de devolución ha expirado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de préstamos atrasados obtenida con éxito")
    })
    public ResponseEntity<List<PrestamoDTO.Response>> buscarPrestamosAtrasados() {
        logger.info("Recibiendo solicitud para buscar prestamos atrasados");
        return ResponseEntity.ok(prestamoService.findPrestamosAtrasados());
    }

    @PatchMapping("/{id}/devolver")
    @Operation(summary = "Registrar devolución de un préstamo", description = "Marca un préstamo como devuelto y actualiza el estado del ejemplar asociado para liberarlo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Devolución registrada exitosamente"),
        @ApiResponse(responseCode = "400", description = "El préstamo ya fue devuelto o no se encuentra en un estado válido"),
        @ApiResponse(responseCode = "404", description = "No se encontró el préstamo indicado")
    })
    public ResponseEntity<PrestamoDTO.Response> registrarDevolucion(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para registrar la devolucion del prestamo ID: {}", id);
        return ResponseEntity.ok(prestamoService.registrarDevolucion(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un préstamo", description = "Modifica los datos de un préstamo existente utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Préstamo actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "El préstamo a actualizar no existe")
    })
    public ResponseEntity<PrestamoDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody PrestamoDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar prestamo con ID: {}", id);
        return ResponseEntity.ok(prestamoService.updatePrestamo(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un préstamo", description = "Remueve permanentemente un registro de préstamo del sistema mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Préstamo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el préstamo a eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar prestamo por ID: {}", id);
        prestamoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}