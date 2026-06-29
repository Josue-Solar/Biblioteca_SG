package com.biblioteca.libro.controller;

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

import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.service.LibroService;

// Importaciones de Swagger
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/libros")
@RequiredArgsConstructor
@Tag(name = "Gestion de Libros", description = "Operaciones relacionadas con el catálogo de libros")
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    private final LibroService libroService;

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Obtiene una lista general de todos los libros registrados en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de libros obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay libros registrados en el sistema")
    })
    public ResponseEntity<List<LibroDTO.Response>> getAllLibs() {
        logger.info("Recibiendo solicitud para listar todos los libros");
        List<LibroDTO.Response> libros = libroService.obtenerTodos();
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{isbn}")
    @Operation(summary = "Buscar libro por ISBN", description = "Obtiene los detalles específicos de un libro utilizando su código ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún libro con el ISBN proporcionado")
    })
    public ResponseEntity<?> getByID(@Valid @PathVariable long isbn) {
        logger.info("Recibiendo solicitud para buscar libro por ISBN: {}", isbn);
        try {
            return ResponseEntity.ok(libroService.obtenerPorIsbn(isbn));
        } catch (Exception ex) {
            logger.error("Error al buscar libro con ISBN {}: {}", isbn, ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{isbn}/autores")
    @Operation(summary = "Obtener autores de un libro", description = "Retorna la lista de autores que escribieron un libro específico basado en su ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autores obtenidos con éxito"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<?> getAutoresPorLibro(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud para buscar autores del libro con ISBN: {}", isbn);
        try {
            return ResponseEntity.ok(libroService.obtenerAutores(isbn));
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{isbn}/genero")
    @Operation(summary = "Obtener género de un libro", description = "Obtiene la información del género literario al que pertenece un libro según su ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Género obtenido con éxito"),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    public ResponseEntity<?> getGenero(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud para obtener el genero del libro con ISBN: {}", isbn);
        try {
            return ResponseEntity.ok(libroService.verGenero(isbn));
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{isbn}/ejemplares")
    @Operation(summary = "Listar ejemplares de un libro", description = "Devuelve todos los ejemplares físicos disponibles o registrados para un ISBN determinado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito")
    })
    public ResponseEntity<?> getEjemplares(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud para listar ejemplares del libro ISBN: {}", isbn);
        return ResponseEntity.ok(libroService.listarEjemplares(isbn));
    }
    
    @GetMapping("/autor/{id}")
    @Operation(summary = "Buscar libros por Autor", description = "Obtiene una lista de todos los libros escritos por un autor específico (búsqueda por ID del autor)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libros encontrados con éxito")
    })
    public ResponseEntity<?> getAllByAuthId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar libros por el ID de Autor: {}", id);
        return ResponseEntity.ok(libroService.listarLibros(id));
    }

    @GetMapping("/genero/{id}")
    @Operation(summary = "Buscar libros por Género", description = "Obtiene una lista de todos los libros que pertenecen a un género literario específico (búsqueda por ID del género)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libros encontrados con éxito")
    })
    public ResponseEntity<?> getAllByGenre(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar libros por el ID de Genero: {}", id);
        return ResponseEntity.ok(libroService.verLibrosPorGenero(id));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo libro", description = "Agrega un nuevo libro al catálogo validando que sus datos sean correctos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error de validación en los datos enviados")
    })
    public ResponseEntity<LibroDTO.Response> addLibro(@Valid @RequestBody LibroDTO.Request lib) {
        logger.info("Recibiendo solicitud para registrar un nuevo libro");
        return ResponseEntity.status(HttpStatus.CREATED).body(libroService.guardar(lib));
    }

    @PutMapping("/{isbn}")
    @Operation(summary = "Actualizar un libro", description = "Modifica los detalles de un libro existente en el catálogo utilizando su ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro actualizado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "El libro a actualizar no existe")
    })
    public ResponseEntity<LibroDTO.Response> putLibro(@Valid @RequestBody LibroDTO.Request request, @PathVariable Long isbn) {
        logger.info("Recibiendo solicitud para actualizar el libro con ISBN: {}", isbn);
        return ResponseEntity.status(HttpStatus.OK).body(libroService.actualizar(isbn, request));
    }

    @DeleteMapping("/{isbn}")
    @Operation(summary = "Eliminar un libro", description = "Remueve un libro del catálogo mediante su ISBN")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Libro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el libro a eliminar")
    })
    public ResponseEntity<Void> deleteLibro(@Valid @PathVariable Long isbn) {
        logger.info("Recibiendo solicitud para eliminar el libro con ISBN: {}", isbn);
        return libroService.eliminar(isbn)
                .map(eliminado -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}