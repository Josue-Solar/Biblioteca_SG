package com.example.edicion.controller;

import java.util.List;
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

import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.service.EdicionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ediciones")
@Tag(name= "Gestion de Ediciones", description= "Operaciones relacionadas con las ediciones")
@RequiredArgsConstructor
public class EdicionController {

private final EdicionService edicionService;

    @GetMapping
    @Operation(summary = "Obtener todas las ediciones", description = "Obtiene una lista de todas las ediciones registradas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ediciones obtenida con éxito"),
        @ApiResponse(responseCode = "204", description = "No hay ediciones registradas en la base de datos")
    })
    public List<EdicionDTO.Response> getAllEdics(){
        return edicionService.obtenerTodos();
    }

    @GetMapping("/librosPorEdicion/{edicionId}")
    @Operation(summary = "Obtener libros por edición", description = "Obtiene una lista de libros asociados a una edición específica mediante el ID de la edición")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de libros obtenida con éxito"),
        @ApiResponse(responseCode = "404", description = "La edición no fue encontrada")
    })
    public ResponseEntity<?> librosPorEdicion(@PathVariable Long edicionId){
        return ResponseEntity.ok(edicionService.librosPorEdicion(edicionId));
    }

    @GetMapping("/id:{id}")
    @Operation(summary = "Buscar edición por ID", description = "Obtiene los detalles de una edición específica a través de su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Edición encontrada"),
        @ApiResponse(responseCode = "404", description = "Edición no encontrada con el ID proporcionado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getByID(@Valid @PathVariable long id){
        try{
            return ResponseEntity.ok(edicionService.obtenerPorId(id));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/edicionesPorEditorial:{id}")
    @Operation(summary = "Obtener ediciones por editorial (Ruta 1)", description = "Obtiene una lista de ediciones asociadas a una editorial específica.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ediciones obtenida con éxito"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<?> getAutoresPorLibro(@PathVariable Long id){
        try{
            return ResponseEntity.ok(edicionService.listarEdiciones(id));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/editorialId/{id}")
    @Operation(summary = "Obtener ediciones por editorial (Ruta 2)", description = "Obtiene una lista de ediciones asociadas a una editorial específica usando una ruta alternativa.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ediciones obtenida con éxito")
    })
    public ResponseEntity<?> getAllByEditorialId(@PathVariable Long id){
        return ResponseEntity.ok(edicionService.listarEdiciones(id));
    }

    @PostMapping
    @Operation(summary = "Crear una nueva edición", description = "Registra una nueva edición en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Edición creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (Error de validación)"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<EdicionDTO.Response> addLibro(@Valid @RequestBody EdicionDTO.Request edic){
        return ResponseEntity.status(HttpStatus.CREATED).body(edicionService.guardar(edic));
    }

    @PutMapping("/editar:{id}")
    @Operation(summary = "Actualizar una edición", description = "Modifica los datos de una edición existente por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Edición actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "La edición a actualizar no fue encontrada")
    })
    public ResponseEntity<EdicionDTO.Response> putEdicion(@Valid @RequestBody EdicionDTO.Request request, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(edicionService.actualizar(id, request));
    }

    @DeleteMapping("/eliminar:{id}")
    @Operation(summary = "Eliminar una edición por ID", description = "Elimina lógicamente o físicamente una edición del sistema utilizando su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Edición eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "La edición con el ID proporcionado no existe")
    })
    public ResponseEntity<Boolean> deleteEdicion(@Valid @PathVariable Long id){
        return edicionService.eliminar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


}
