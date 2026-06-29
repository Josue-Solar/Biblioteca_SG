package com.biblioteca.libro.controller;

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

import com.biblioteca.libro.assemblers.LibroModelAssembler;
import com.biblioteca.libro.dto.LibroDTO;
import com.biblioteca.libro.service.LibroService;

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
@RequestMapping("/api/v2/libros")
@RequiredArgsConstructor
@Tag(name = "Gestion de Libros V2 (HATEOAS)", description = "Operaciones del catálogo de libros con navegación hipermedia (HAL+JSON)")
public class LibroControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(LibroControllerV2.class);

    private final LibroService libroService;
    private final LibroModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener todos los libros (HATEOAS)", description = "Retorna una colección hipermedia con todos los libros del catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Colección de libros obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay libros registrados")
    })
    public ResponseEntity<CollectionModel<EntityModel<LibroDTO.Response>>> getAllLibs() {
        logger.info("Recibiendo solicitud V2 para listar todos los libros");
        List<LibroDTO.Response> libros = libroService.obtenerTodos();
        
        if (libros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EntityModel<LibroDTO.Response>> entityModels = libros.stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(entityModels,
                linkTo(methodOn(LibroControllerV2.class).getAllLibs()).withSelfRel()));
    }

    @GetMapping(value = "/{isbn}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar libro por ISBN (HATEOAS)")
    public ResponseEntity<EntityModel<LibroDTO.Response>> getByID(@Valid @PathVariable long isbn) {
        logger.info("Recibiendo solicitud V2 para buscar libro por ISBN: {}", isbn);
        try {
            LibroDTO.Response libro = libroService.obtenerPorIsbn(isbn); // Asumiendo mapeo directo a Response en Service
            return ResponseEntity.ok(assembler.toModel(libro));
        } catch (Exception ex) {
            logger.error("Error V2 al buscar libro con ISBN {}: {}", isbn, ex.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{isbn}/autores", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener autores de un libro (HATEOAS)")
    public ResponseEntity<EntityModel<?>> getAutoresPorLibro(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud V2 para buscar autores del libro con ISBN: {}", isbn);
        try {
            Object autores = libroService.obtenerAutores(isbn);
            EntityModel<?> entityModel = EntityModel.of(autores,
                    linkTo(methodOn(LibroControllerV2.class).getAutoresPorLibro(isbn)).withSelfRel(),
                    linkTo(methodOn(LibroControllerV2.class).getByID(isbn)).withRel("libro-detalle"));
            return ResponseEntity.ok(entityModel);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{isbn}/genero", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Obtener género de un libro (HATEOAS)")
    public ResponseEntity<EntityModel<?>> getGenero(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud V2 para obtener el genero del libro con ISBN: {}", isbn);
        try {
            Object genero = libroService.verGenero(isbn);
            EntityModel<?> entityModel = EntityModel.of(genero,
                    linkTo(methodOn(LibroControllerV2.class).getGenero(isbn)).withSelfRel(),
                    linkTo(methodOn(LibroControllerV2.class).getByID(isbn)).withRel("libro-detalle"));
            return ResponseEntity.ok(entityModel);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/{isbn}/ejemplares", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Listar ejemplares de un libro (HATEOAS)")
    public ResponseEntity<EntityModel<?>> getEjemplares(@PathVariable Long isbn) {
        logger.info("Recibiendo solicitud V2 para listar ejemplares del libro ISBN: {}", isbn);
        Object ejemplares = libroService.listarEjemplares(isbn);
        
        EntityModel<?> entityModel = EntityModel.of(ejemplares,
                linkTo(methodOn(LibroControllerV2.class).getEjemplares(isbn)).withSelfRel(),
                linkTo(methodOn(LibroControllerV2.class).getByID(isbn)).withRel("libro-detalle"));
        return ResponseEntity.ok(entityModel);
    }
    
    @GetMapping(value = "/autor/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar libros por Autor (HATEOAS)")
    public ResponseEntity<EntityModel<?>> getAllByAuthId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para buscar libros por el ID de Autor: {}", id);
        Object libros = libroService.listarLibros(id);
        
        EntityModel<?> entityModel = EntityModel.of(libros,
                linkTo(methodOn(LibroControllerV2.class).getAllByAuthId(id)).withSelfRel(),
                linkTo(methodOn(LibroControllerV2.class).getAllLibs()).withRel("todos-los-libros"));
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping(value = "/genero/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Buscar libros por Género (HATEOAS)")
    public ResponseEntity<EntityModel<?>> getAllByGenre(@PathVariable Long id) {
        logger.info("Recibiendo solicitud V2 para buscar libros por el ID de Genero: {}", id);
        Object libros = libroService.verLibrosPorGenero(id);
        
        EntityModel<?> entityModel = EntityModel.of(libros,
                linkTo(methodOn(LibroControllerV2.class).getAllByGenre(id)).withSelfRel(),
                linkTo(methodOn(LibroControllerV2.class).getAllLibs()).withRel("todos-los-libros"));
        return ResponseEntity.ok(entityModel);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Registrar un nuevo libro (HATEOAS)")
    public ResponseEntity<EntityModel<LibroDTO.Response>> addLibro(@Valid @RequestBody LibroDTO.Request lib) {
        logger.info("Recibiendo solicitud V2 para registrar un nuevo libro");
        LibroDTO.Response nuevoLibro = libroService.guardar(lib);
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevoLibro));
    }

    @PutMapping(value = "/{isbn}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "Actualizar un libro (HATEOAS)")
    public ResponseEntity<EntityModel<LibroDTO.Response>> putLibro(@Valid @RequestBody LibroDTO.Request request, @PathVariable Long isbn) {
        logger.info("Recibiendo solicitud V2 para actualizar el libro con ISBN: {}", isbn);
        LibroDTO.Response libroActualizado = libroService.actualizar(isbn, request);
        return ResponseEntity.ok(assembler.toModel(libroActualizado));
    }

    @DeleteMapping("/{isbn}")
    @Operation(summary = "Eliminar un libro")
    public ResponseEntity<Void> deleteLibro(@Valid @PathVariable Long isbn) {
        logger.info("Recibiendo solicitud V2 para eliminar el libro con ISBN: {}", isbn);
        return libroService.eliminar(isbn)
                .map(eliminado -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }
}