package com.biblioteca.editorial.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.validation.Valid;

import com.biblioteca.editorial.dto.EditorialDTO;
import com.biblioteca.editorial.service.EditorialService;

@RestController
@RequestMapping("/api/v1/editoriales")
public class EditorialController {

    private static final Logger logger = LoggerFactory.getLogger(EditorialController.class.getName());

    @Autowired
    private EditorialService editorialService;

    @GetMapping
    public ResponseEntity<List<EditorialDTO.Response>> listar() {
        logger.info("Recibiendo solicitud para listar editoriales");//log
        List<EditorialDTO.Response> editoriales = editorialService.findAll();
        if(editoriales.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(editoriales);
    }

    @PostMapping
    public ResponseEntity<EditorialDTO.Response> guardar(@Valid @RequestBody EditorialDTO.Request request){
        logger.info("Recibiendo solicitud para guardar comuna");//log
            EditorialDTO.Response response = editorialService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para borrar comuna por id");//log
        editorialService.delete(id); //si falla va al global
        return ResponseEntity.noContent().build();
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<EditorialDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar editorial por id");//log
        EditorialDTO.Response response = editorialService.findByIdOrThrow(id); //Si falla, va al GlobalExceptionHandler
        return ResponseEntity.ok(response);
    }

    // buscar por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<EditorialDTO.Response> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar comunas por nombre");//log
        Optional<EditorialDTO.Response> response = editorialService.findByNombre(nombre);
        return response
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<EditorialDTO.Response> 
            actualizar(@PathVariable Long id, @Valid @RequestBody EditorialDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Editorial por ID: " + id);
        return ResponseEntity.ok(editorialService.update(id, request));
    }

    

}
