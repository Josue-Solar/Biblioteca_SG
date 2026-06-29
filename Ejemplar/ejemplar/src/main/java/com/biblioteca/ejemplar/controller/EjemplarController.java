package com.biblioteca.ejemplar.controller;

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

import com.biblioteca.ejemplar.model.Ejemplar;
import com.biblioteca.ejemplar.service.EjemplarService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ejemplares")
@RequiredArgsConstructor
// @Tag define el nombre y descripción del módulo en la interfaz de Swagger UI
@Tag(name = "Ejemplar Controller", description = "API para la gestión, control y reservas de ejemplares en la biblioteca")
public class EjemplarController {

    private final EjemplarService ejemplarService;

    @GetMapping
    @Operation(summary = "Obtener todos los ejemplares", description = "Retorna una lista con todos los ejemplares registrados en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito")
    })
    public List<Ejemplar> getAllEjemplares(){
        return ejemplarService.obtenerTodos();
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Obtener ejemplar por ID", description = "Busca y retorna un ejemplar específico utilizando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar encontrado con éxito"),
        @ApiResponse(responseCode = "404", description = "El ejemplar con el ID proporcionado no existe")
    })
    public ResponseEntity<Ejemplar> getByID(@PathVariable("id") long id){
        return ejemplarService.obtenerPorId(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/traeLibro/{id}")
    @Operation(summary = "Obtener el libro de un ejemplar", description = "Retorna la información del libro asociado al ID del ejemplar suministrado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información del libro obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "Ejemplar o libro no encontrado")
    })
    public ResponseEntity<?> getLibro(@PathVariable("id") Long id){
        return ResponseEntity.ok(ejemplarService.getLibro(id));
    }

    @GetMapping("/porISBN/{isbn}")
    @Operation(summary = "Obtener ejemplares por ISBN", description = "Retorna una lista de ejemplares que corresponden a un número de ISBN específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito")
    })
    public ResponseEntity<?> getAllByISBN(@PathVariable("isbn") Long isbn){
        return ResponseEntity.ok(ejemplarService.obtenerTodosPorIsbn(isbn));
    }

    @GetMapping("/porEdicion/{edicionId}")
    @Operation(summary = "Obtener ejemplares por ID de edición", description = "Retorna una lista de ejemplares pertenecientes a una edición en particular.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ejemplares obtenida con éxito")
    })
    public ResponseEntity<?> getAllByEdicionId(@PathVariable("edicionId") Long edicionId){
        return ResponseEntity.ok(ejemplarService.obtenerTodosPorEdicionId(edicionId));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo ejemplar", description = "Crea y almacena un nuevo ejemplar en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ejemplar creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El cuerpo de la petición contiene datos inválidos o mal estructurados")
    })
    public ResponseEntity<Ejemplar> saveEntity(@Valid @RequestBody Ejemplar ejemplar){
        return ResponseEntity.status(HttpStatus.CREATED).body(ejemplarService.guardar(ejemplar));
    }
    
    @PutMapping("/actualizar/{id}")
    @Operation(summary = "Actualizar un ejemplar existente", description = "Modifica los datos o el estado de reserva de un ejemplar según su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ejemplar actualizado con éxito"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos"),
        @ApiResponse(responseCode = "404", description = "Ejemplar no encontrado")
    })
    public ResponseEntity<?> updateEjemplar(@PathVariable("id") Long id, @Valid @RequestBody Ejemplar ejemplar){
        return ResponseEntity.ok(ejemplarService.modReserva(id, ejemplar));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un ejemplar", description = "Elimina físicamente un ejemplar de la base de datos utilizando su identificador único.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Ejemplar eliminado exitosamente (No Content)"),
        @ApiResponse(responseCode = "404", description = "El ejemplar a eliminar no fue encontrado")
    })
    public ResponseEntity<?> deleteById(@PathVariable("id") Long id){
        ejemplarService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}