package com.biblioteca.editorial.controller;


import java.util.List;

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

import com.biblioteca.editorial.model.Editorial;
import com.biblioteca.editorial.service.EditorialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/editoriales")
public class EditorialController {

    private static final Logger logger = LoggerFactory.getLogger(EditorialController.class.getName());
    
    @Autowired
    private EditorialService editorialService;

    @GetMapping
    public ResponseEntity<Object> listar(){
        logger.info("Recibiendo solicitud para listar editoriales");//log
        List<Editorial> editoriales = editorialService.obtenerTodos();
        if(editoriales.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(editoriales);
    }

    @GetMapping("/ediciones/{editorialId}")
    public ResponseEntity<?> listarEdiciones(@PathVariable Long editorialId){
        return ResponseEntity.ok(editorialService.listarEdiciones(editorialId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Editorial> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar editorial por ID: " + id);//log
        try {
            Editorial editorial = editorialService.findByIdOrThrow(id);
            return ResponseEntity.ok(editorial);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombre}") //buscar por apellido
    public ResponseEntity<List<Editorial>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar editorial por nombre: " + nombre);//log
        List<Editorial> editoriales = editorialService.obtenerPorNombre(nombre);
        if (editoriales.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(editoriales);
    }

    @PostMapping
    public ResponseEntity<Editorial> crear(@RequestBody Editorial editorial) {
        logger.info("Recibiendo solicitud para crear editorial");//log
        Editorial nuevaEditorial = editorialService.guardar(editorial);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEditorial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizar(@PathVariable long id, @Valid @RequestBody Editorial editorial) {
        logger.info("Recibiendo solicitud para actualizar editorial" + id);
        Editorial editorialActualizada = editorialService.modificarEditorial(id, editorial);  
        if (editorialActualizada != null) {
            return ResponseEntity.ok(editorialActualizada);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        logger.info("Recibiendo solicitud para eliminar editorial por ID: " + id);//log
        try {
            editorialService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
