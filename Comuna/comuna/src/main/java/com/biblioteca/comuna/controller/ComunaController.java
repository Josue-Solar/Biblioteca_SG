package com.biblioteca.comuna.controller;

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

import com.biblioteca.comuna.dto.ComunaDTO;
import com.biblioteca.comuna.service.ComunaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/comunas")
@Tag(name= "Gestion de Comunas", description= "Operaciones relacionadas con las comunas")
@RequiredArgsConstructor
public class ComunaController {

    private static final Logger logger = LoggerFactory.getLogger(ComunaController.class.getName());

    private final ComunaService comunaService;

    @GetMapping
    @Operation(summary = "Obtener todas las comunas", description = "Obtiene una lista de todas las comunas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de comunas obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay comunas registradas en la base de datos")
    })
    public ResponseEntity<List<ComunaDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar comunas");//log
        List<ComunaDTO.Response> comunas = comunaService.findAll();
        if(comunas.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(comunas);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva comuna", description = "Registra una nueva comuna en el sistema. El nombre es obligatorio.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Comuna creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Error de validación)"),
        @ApiResponse(responseCode = "500", description = "Error interno o violación de unicidad de clave")
    })
    public ResponseEntity<ComunaDTO.Response> guardar(@Valid @RequestBody ComunaDTO.Request request){
        logger.info("Recibiendo solicitud para guardar comuna");//log
            ComunaDTO.Response response = comunaService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una comuna por ID", description = "Elimina fisicamente una comuna del sistema utilizando su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Comuna eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "La comuna con el ID proporcionado no existe")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar comuna por id");//log
        comunaService.delete(id); //si falla va al global
        return ResponseEntity.noContent().build();
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar comuna por ID", description = "Obtiene los detalles de una comuna específica a través de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna encontrada"),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada con el ID proporcionado")
    })
    public ResponseEntity<ComunaDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar comuna por id");//log
        ComunaDTO.Response response = comunaService.findByIdOrThrow(id); //Si falla, va al GlobalExceptionHandler
        return ResponseEntity.ok(response);
    }

    // buscar por nombre
    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Buscar comuna por nombre", description = "Busca los detalles de una comuna ingresando su nombre.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna encontrada"),
        @ApiResponse(responseCode = "404", description = "No se encontro ninguna comuna con ese nombre")
    })
    public ResponseEntity<ComunaDTO.Response> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar comunas por nombre");//log
        Optional<ComunaDTO.Response> response = comunaService.findByNombre(nombre);
        return response
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    @Operation(summary = "Actualizar una comuna", description = "Modifica los datos de una comuna existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualizacion invalidos"),
        @ApiResponse(responseCode = "404", description = "La comuna a actualizar no fue encontrada")
    })
    public ResponseEntity<ComunaDTO.Response> 
            actualizar(@PathVariable Long id, @Valid @RequestBody ComunaDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Comuna por ID: " + id);
        return ResponseEntity.ok(comunaService.update(id, request));
    }

    /*@PutMapping("/{id}")
    public ResponseEntity<ComunaDTO.Response> actualizar(@PathVariable Long id, @Valid @RequestBody ComunaDTO.Request request) {
    logger.info("Recibiendo solicitud para actualizar Comuna por ID: {}", id);
    try {
        ComunaDTO.Response response = comunaService.update(id, request);
        
        // Si todo sale bien, devolvemos 200 OK
        return ResponseEntity.ok(response);
    } catch (Exception ex) {
        // Si el service lanzó el RuntimeException, caemos aquí y devolvemos 404
        logger.error("Error al actualizar la comuna: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
        }
    } */

}
