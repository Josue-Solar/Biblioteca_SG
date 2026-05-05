package com.biblioteca.autor.controller;

import java.util.List;

<<<<<<< HEAD
=======
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

>>>>>>> usuario
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.PutMapping;
>>>>>>> usuario
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.service.AutorService;

<<<<<<< HEAD
=======
import jakarta.validation.Valid;

>>>>>>> usuario
@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

<<<<<<< HEAD
=======
    private static final Logger logger = LoggerFactory.getLogger(AutorController.class.getName());

>>>>>>> usuario
    @Autowired
    private AutorService autorService;

    // ver todos
    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
<<<<<<< HEAD
=======
        logger.info("Recibiendo solicitud para listar autores");//log
>>>>>>> usuario
        List<Autor> autores = autorService.findAll();
        if(autores.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    //crear
    @PostMapping
<<<<<<< HEAD
    public ResponseEntity<Autor> 
        guardar(@RequestBody Autor autor){
            Autor nAutor = 
                autorService.save(autor);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(nAutor);
=======
    public ResponseEntity<Autor> guardar(@Valid @RequestBody Autor autor){
        logger.info("Recibiendo solicitud para guardar autor");//log
            Autor nAutor = autorService.save(autor);
            return ResponseEntity.status(HttpStatus.CREATED).body(nAutor);
>>>>>>> usuario
    }

    //borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
<<<<<<< HEAD
=======
        logger.info("Recibiendo solicitud para eliminar autor");//log
>>>>>>> usuario
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
<<<<<<< HEAD
=======
        logger.info("Recibiendo solicitud para buscar autor por ID");//log
>>>>>>> usuario
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
<<<<<<< HEAD
=======
        logger.info("Recibiendo solicitud para buscar autor por el primero apellido");//log
>>>>>>> usuario
        List<Autor> autores = autorService.findByApPaterno(apellido);
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

<<<<<<< HEAD
=======
    @PutMapping("/{id}") // Actualizar por ID
    public ResponseEntity<Autor> actualizar(@PathVariable Long id, @Valid @RequestBody Autor autor) {
        logger.info("Recibiendo solicitud para actualizar Autor por ID: " + id);
        Autor autorActualizado = autorService.updateAutor(id, autor);  
        if (autorActualizado != null) {
            return ResponseEntity.ok(autorActualizado);
        }
        return ResponseEntity.notFound().build();
    }

>>>>>>> usuario
}
