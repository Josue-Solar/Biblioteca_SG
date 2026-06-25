package com.biblioteca.autor.controller;

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

import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.service.AutorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/autores")
@Tag(name= "Gestion de Autores", description= "Operaciones relacionadas con los autores")
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class.getName());

    private final AutorService autorService;

    AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    // ver todos
    @GetMapping
    @Operation(summary = "Obtener todos los autores", description = "Obtiene una lista de todos los autores")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de autores obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay autores registrados en la base de datos")
    })
    public ResponseEntity<List<AutorDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar autores");//log
        List<AutorDTO.Response> autores = autorService.findAll();
        if(autores.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    //crear
    @PostMapping
    @Operation(summary = "Crear un nuevo autor", description = "Registra un nuevo autor en el sistema. El nombre es obligatorio.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Autor creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Error de validación)"),
        @ApiResponse(responseCode = "500", description = "Error interno o violación de unicidad de clave")
    })
    public ResponseEntity<AutorDTO.Response> guardar(@Valid @RequestBody AutorDTO.Request request){
        logger.info("Recibiendo solicitud para guardar autor");//log
            AutorDTO.Response response = autorService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //borrar
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un autor por ID", description = "Elimina fisicamente un autor del sistema utilizando su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Autor eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "El autor con el ID proporcionado no existe")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para eliminar autor");//log
        autorService.delete(id);
        return ResponseEntity.noContent().build();   
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar autor por ID", description = "Obtiene los detalles de un autor específico a través de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor encontrado"),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado con el ID proporcionado")
    })
    public ResponseEntity<AutorDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar autor por ID");//log
            return ResponseEntity.ok(autorService.findByIdOrThrow(id));
    }

    //buscar por apellido
    @GetMapping("/apellido/{apellido}")
    @Operation(summary = "Buscar autor por apellido", description = "Busca los detalles de un autor ingresando su apellido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor encontrado"),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún autor con ese apellido")
    })
    public ResponseEntity<List<AutorDTO.Response>> buscarPorApellido(@PathVariable String apellido) {
        logger.info("Recibiendo solicitud para buscar autor por el primer apellido");//log
        List<AutorDTO.Response> autores = autorService.findByApPaterno(apellido);
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    //buscar por nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar autor por nombre", description = "Busca los detalles de un autor ingresando su nombre.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor encontrado"),
        @ApiResponse(responseCode = "404", description = "No se encontró ningún autor con ese nombre")
    })
    public ResponseEntity<List<AutorDTO.Response>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar autor por el primer nombre");//log
        List<AutorDTO.Response> autores = autorService.findByPrimerNombre(nombre);
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}") // Actualizar por ID
    @Operation(summary = "Actualizar un autor", description = "Modifica los datos de un autor existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualizacion invalidos"),
        @ApiResponse(responseCode = "404", description = "El autor a actualizar no fue encontrado")
    })
    public ResponseEntity<AutorDTO.Response> 
            actualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Autor por ID: " + id);
            return ResponseEntity.ok(autorService.updateAutor(id, request));
    }

}
