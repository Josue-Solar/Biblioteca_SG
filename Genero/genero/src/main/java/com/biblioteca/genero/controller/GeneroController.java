package com.biblioteca.genero.controller;

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

@RestController
@RequestMapping("/api/v1/generos")
@RequiredArgsConstructor 
@Tag(name = "Gestion de Generos", description = "Operaciones relacionadas con los géneros literarios de los libros")
public class GeneroController {

    private static final Logger logger = LoggerFactory.getLogger(GeneroController.class);

    private final GeneroService generoService; // Ahora es 'final'

    @GetMapping
    @Operation(summary = "Obtener todos los géneros", description = "Obtiene una lista general de todos los géneros literarios registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de géneros obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay géneros registrados en la base de datos")
    })
    public ResponseEntity<List<Genero>> listar() {
        logger.info("Recibiendo solicitud para listar generos");
        List<Genero> generos = generoService.obtenerTodos();
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar géneros por nombre", description = "Busca y devuelve una lista de géneros que coincidan con el nombre proporcionado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Géneros encontrados con éxito"),
        @ApiResponse(responseCode = "204", description = "No se encontraron géneros con ese nombre")
    })
    public ResponseEntity<List<GeneroDTO.Response>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar genero por nombre: {}", nombre);
        List<GeneroDTO.Response> generos = generoService.obtenerPorNombre(nombre);
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar género por ID", description = "Obtiene los detalles específicos de un género mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Género encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró el género con el ID proporcionado")
    })
    public ResponseEntity<GeneroDTO.Response> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar genero por ID: {}", id);
        try {
            GeneroDTO.Response genero = generoService.findByIdOrThrow(id);
            return ResponseEntity.ok(genero);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{generoId}/libros")
    @Operation(summary = "Obtener libros de un género", description = "Busca y retorna todos los libros asociados a un género literario específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de libros obtenida con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró el género o no tiene libros asociados")
    })
    public ResponseEntity<GeneroLibroDTO> libroPorGenero(@PathVariable long generoId) {
        logger.info("Recibiendo solicitud para buscar libros por el genero ID: {}", generoId);
        try {
            GeneroLibroDTO generoLibroDTO = generoService.librosPorGenero(generoId);
            return ResponseEntity.ok(generoLibroDTO);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo género", description = "Registra un nuevo género literario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Género creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    public ResponseEntity<GeneroDTO.Response> crear(@Valid @RequestBody GeneroDTO.Request genero) {
        logger.info("Recibiendo solicitud para guardar nuevo genero");
        GeneroDTO.Response nuevoGenero = generoService.guardar(genero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGenero);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un género", description = "Modifica los datos de un género literario existente utilizando su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Género actualizado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "El género que se intenta actualizar no existe")
    })
    public ResponseEntity<GeneroDTO.Response> actualizar(@PathVariable long id, @Valid @RequestBody GeneroDTO.Request genero) {
        logger.info("Recibiendo solicitud para actualizar genero ID: {}", id);
        GeneroDTO.Response generoActualizado = generoService.modificarGenero(id, genero);  
        if (generoActualizado != null) {
            return ResponseEntity.ok(generoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un género", description = "Remueve de forma permanente un género del sistema mediante su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Género eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el género a eliminar o no se pudo eliminar por dependencias")
    })
    public ResponseEntity<Void> eliminarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para eliminar genero por ID: {}", id);
        try {
            generoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al intentar eliminar genero por ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}