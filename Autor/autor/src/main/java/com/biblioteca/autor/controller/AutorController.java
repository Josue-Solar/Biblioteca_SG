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

import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.service.AutorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/autores")
@RequiredArgsConstructor
public class AutorController {

    private static final Logger logger = LoggerFactory.getLogger(AutorController.class.getName());

    private final AutorService autorService;

    // ver todos
    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        logger.info("Recibiendo solicitud para listar autores");//log
        List<Autor> autores = autorService.findAll();
        if(autores.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    @GetMapping("/libros/{autorId}")
    public ResponseEntity<?> listarLibros(@PathVariable Long autorId){
        logger.info("Recibiendo solicitud para buscar libros por autor: " + autorId);//log
        return ResponseEntity.ok(autorService.listarLibros(autorId));
    }

    //crear
    @PostMapping
    public ResponseEntity<Autor> guardar(@Valid @RequestBody Autor autor){
        logger.info("Recibiendo solicitud para guardar autor");//log
        Autor nAutor = autorService.save(autor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nAutor);
    }

    //borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        logger.info("Recibiendo solicitud para eliminar autor");//log
        try{
            autorService.delete(id);
            return ResponseEntity.noContent().build();
        }catch(Exception ex){
            return ResponseEntity.notFound().build();
        }
    }

    // Buscar por ID
    @GetMapping("/id/{id}")
    public ResponseEntity<Autor> buscarPorId(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para buscar autor por ID");//log
        try {
            Autor autor = autorService.findByIdOrThrow(id);
            return ResponseEntity.ok(autor);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    //buscar por apellido
    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Autor>> buscarPorApellido(@PathVariable String apellido) {
        logger.info("Recibiendo solicitud para buscar autor por el primero apellido");//log
        List<Autor> autores = autorService.findByApPaterno(apellido);
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<Autor> actualizar(@PathVariable Long id, @Valid @RequestBody Autor autor) {
        logger.info("Recibiendo solicitud para actualizar Autor por ID: " + id);
        Autor autorActualizado = autorService.updateAutor(id, autor);  
        if (autorActualizado != null) {
            return ResponseEntity.ok(autorActualizado);
        }
        return ResponseEntity.notFound().build();
    }

}
