package com.biblioteca.editorial.controller;

import java.util.List;
import java.util.Optional;

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

import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.service.EditorialService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/editoriales")
@RequiredArgsConstructor // Reemplaza a @Autowired (Mejor práctica)
@Tag(name = "Gestion de Editoriales", description = "Operaciones relacionadas con las editoriales")
public class EditorialController {

    private static final Logger logger = LoggerFactory.getLogger(EditorialController.class.getName());

    private final EditorialService editorialService; // Ahora es 'final' por el RequiredArgsConstructor

    @GetMapping
    @Operation(summary = "Obtener todas las editoriales", description = "Obtiene una lista de todas las editoriales registradas en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de editoriales obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay editoriales registradas en la base de datos")
    })
    public ResponseEntity<List<EditorialDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar editoriales");
        List<EditorialDTO.Response> editoriales = editorialService.findAll();
        if(editoriales.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(editoriales);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva editorial", description = "Registra una nueva editorial en el sistema validando sus datos de entrada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Editorial creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Error de validación)")
    })
    public ResponseEntity<EditorialDTO.Response> guardar(@Valid @RequestBody EditorialDTO.Request request) {
        logger.info("Recibiendo solicitud para guardar una nueva editorial");
        EditorialDTO.Response response = editorialService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una editorial", description = "Elimina una editorial del sistema utilizando su identificador único (ID)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Editorial eliminada exitosamente (Sin contenido)"),
        @ApiResponse(responseCode = "404", description = "La editorial con el ID proporcionado no existe")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar editorial por id");
        editorialService.delete(id); 
        return ResponseEntity.noContent().build();
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar editorial por ID", description = "Obtiene los detalles específicos de una editorial buscando por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Editorial encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró la editorial con el ID proporcionado")
    })
    public ResponseEntity<EditorialDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar editorial por id");
        EditorialDTO.Response response = editorialService.findByIdOrThrow(id); 
        return ResponseEntity.ok(response);
    }

    // buscar por nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar editorial por nombre", description = "Busca y devuelve los detalles de una editorial coincidente con el nombre indicado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Editorial encontrada con éxito"),
        @ApiResponse(responseCode = "404", description = "No se encontró ninguna editorial con ese nombre")
    })
    public ResponseEntity<EditorialDTO.Response> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar editorial por nombre");
        Optional<EditorialDTO.Response> response = editorialService.findByNombre(nombre);
        return response
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    @Operation(summary = "Actualizar una editorial", description = "Modifica los datos de una editorial existente según su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Editorial actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos (Error de validación)"),
        @ApiResponse(responseCode = "404", description = "La editorial a actualizar no fue encontrada")
    })
    public ResponseEntity<EditorialDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody EditorialDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Editorial por ID: " + id);
        return ResponseEntity.ok(editorialService.update(id, request));
    }

}


