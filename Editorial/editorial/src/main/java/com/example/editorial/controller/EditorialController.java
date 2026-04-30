package com.example.editorial.controller;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

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

import com.example.editorial.model.Editorial;
import com.example.editorial.service.EditorialService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/editoriales")
public class EditorialController {

    private static final Logger logger = Logger.getLogger(EditorialController.class.getName());
    
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


    @GetMapping("/{id}")
    public ResponseEntity<Long> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar editorial por ID: " + id);//log
        Optional<Editorial> editoriales = editorialService.obtenerPorId(id);
        if (editoriales == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(id);
    }

    @PostMapping
    public ResponseEntity<Editorial> crear(@RequestBody Editorial editorial) {
        logger.info("Recibiendo solicitud para crear editorial");//log
        Editorial nuevaEditorial = editorialService.guardar(editorial);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEditorial);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Editorial> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Editorial datos) {
        logger.info("Recibiendo solicitud para actualizar editorial");
        return editorialService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar editorial por ID: " + id);//log
        try {
            editorialService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
