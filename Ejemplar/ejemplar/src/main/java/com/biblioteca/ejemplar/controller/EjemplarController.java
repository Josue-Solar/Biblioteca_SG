package com.biblioteca.ejemplar.controller;

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

import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.service.EjemplarService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ejemplares")
@RequiredArgsConstructor
@Tag(name = "Gestion de Ejemplares", description = "Operaciones relacionadas con los ejemplares físicos de los libros")
public class EjemplarController {

    private static final Logger logger = LoggerFactory.getLogger(EjemplarController.class);

    private final EjemplarService ejemplarService;

    @GetMapping
    @Operation(summary = "Obtener todos los ejemplares", description = "Obtiene una lista de todos los ejemplares registrados en la biblioteca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay ejemplares registrados")
    })
    public ResponseEntity<List<Ejemplar>> getAllEjemplares() {
        logger.info("Recibiendo solicitud para listar todos los ejemplares");
        List<Ejemplar> ejemplares = ejemplarService.obtenerTodos();
        if(ejemplares.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ejemplares);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar ejemplar por ID", description = "Obtiene los detalles específicos de un ejemplar mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró el ejemplar con el ID proporcionado")
    })
    public ResponseEntity<Ejemplar> getByID(@Valid @PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar ejemplar por ID: {}", id);
        return ejemplarService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/libro")
    @Operation(summary = "Obtener el libro de un ejemplar", description = "Busca y retorna la información del libro asociado a un ejemplar específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro obtenido con éxito"),
        @ApiResponse(responseCode = "404", description = "Ejemplar o libro no encontrado")
    })
    public ResponseEntity<?> getLibro(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para obtener el libro del ejemplar ID: {}", id);
        return ResponseEntity.ok(ejemplarService.getLibro(id));
    }

    @GetMapping("/isbn/{isbn}")
    @Operation(summary = "Buscar ejemplares por ISBN", description = "Obtiene una lista de todos los ejemplares que corresponden a un ISBN de libro específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito")
    })
    public ResponseEntity<?> getAllByISBN(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud para buscar ejemplares por ISBN: {}", isbn);
        return ResponseEntity.ok(ejemplarService.obtenerTodosPorIsbn(isbn));
    }

    @GetMapping("/edicion/{edicionId}")
    @Operation(summary = "Buscar ejemplares por Edición", description = "Obtiene una lista de ejemplares asociados a una edición específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito")
    })
    public ResponseEntity<?> getAllByEdicionId(@PathVariable Long edicionId) {
        logger.info("Recibiendo solicitud para buscar ejemplares por ID de Edición: {}", edicionId);
        return ResponseEntity.ok(ejemplarService.obtenerTodosPorEdicionId(edicionId));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo ejemplar", description = "Registra un nuevo ejemplar en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ejemplar creado de forma exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    public ResponseEntity<Ejemplar> saveEntity(@Valid @RequestBody Ejemplar ejemplar) {
        logger.info("Recibiendo solicitud para guardar un nuevo ejemplar");
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarService.guardar(ejemplar));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un ejemplar", description = "Modifica los datos de un ejemplar existente utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "El ejemplar que se intenta actualizar no existe")
    })
    public ResponseEntity<?> updateEjemplar(@PathVariable Long id, @Valid @RequestBody Ejemplar ejemplar) {
        logger.info("Recibiendo solicitud para actualizar el ejemplar ID: {}", id);
        return ResponseEntity.ok(ejemplarService.modReserva(id, ejemplar));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un ejemplar", description = "Remueve un ejemplar del sistema mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el ejemplar a eliminar")
    })
    public ResponseEntity<?> deleteEjemplar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar el ejemplar ID: {}", id);
        return (ResponseEntity<?>) ResponseEntity.ok();
    }
}
