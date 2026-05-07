package com.biblioteca.genero.controller;

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

import com.biblioteca.genero.model.Genero;
import com.biblioteca.genero.service.GeneroService;


@RestController
@RequestMapping("/api/v1/generos")
public class GeneroController {

    private static final Logger logger = LoggerFactory.getLogger(GeneroController.class.getName());

    @Autowired
    private GeneroService generoService;

    @GetMapping
    public ResponseEntity<List<Genero>> listar() {
        logger.info("Recibiendo solicitud para listar generos");//log
        List<Genero> generos = generoService.obtenerTodos();
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }

    @GetMapping("/nombre/{nombre}") //buscar por apellido
    public ResponseEntity<List<Genero>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar genero por nombre: " + nombre);//log
        List<Genero> generos = generoService.obtenerPorNombre(nombre);
        if (generos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(generos);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Genero> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar genero por ID: " + id);//log
        try {
            Genero genero = generoService.findByIdOrThrow(id);
            return ResponseEntity.ok(genero);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Genero> crear(@RequestBody Genero genero) {
        logger.info("Recibiendo solicitud para guardar genero");//log
        Genero nuevoGenero = generoService.guardar(genero);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoGenero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Genero> actualizar(@PathVariable long id, @Valid @RequestBody Genero genero) {
        logger.info("Recibiendo solicitud para actualizar genero" + id);
        Genero generoActualizado = generoService.modificarEdicion(id, genero);  
        if (generoActualizado != null) {
            return ResponseEntity.ok(generoActualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para eliminar genero por id: " + id);//log
        try {
            generoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            //logger.severe("Error al intentar eliminar persona por id: " + id);//log
            logger.error("Error al intentar eliminar genero por id: " + id);//log
            return ResponseEntity.notFound().build();
        }
    }
}
