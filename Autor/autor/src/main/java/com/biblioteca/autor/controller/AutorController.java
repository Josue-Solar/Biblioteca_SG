package com.biblioteca.autor.controller;

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

import com.biblioteca.autor.dto.AutorDTO;
import com.biblioteca.autor.service.AutorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class.getName());

    @Autowired
    private AutorService autorService;

    // ver todos
    @GetMapping
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
    public ResponseEntity<AutorDTO.Response> guardar(@Valid @RequestBody AutorDTO.Request request){
        logger.info("Recibiendo solicitud para guardar autor");//log
            AutorDTO.Response response = autorService.save(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para eliminar autor");//log
        autorService.delete(id);
        return ResponseEntity.noContent().build();   
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<AutorDTO.Response> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar autor por ID");//log
            return ResponseEntity.ok(autorService.findByIdOrThrow(id));
    }

    //buscar por apellido
    @GetMapping("/apellido/{apellido}")
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
    public ResponseEntity<List<AutorDTO.Response>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar autor por el primer nombre");//log
        List<AutorDTO.Response> autores = autorService.findByPrimerNombre(nombre);
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<AutorDTO.Response> 
            actualizar(@PathVariable Long id, @Valid @RequestBody AutorDTO.Request request) {
        logger.info("Recibiendo solicitud para actualizar Autor por ID: " + id);
            return ResponseEntity.ok(autorService.updateAutor(id, request));
    }

}
