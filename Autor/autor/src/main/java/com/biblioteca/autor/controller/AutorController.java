package com.biblioteca.autor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.autor.model.Autor;
import com.biblioteca.autor.service.AutorService;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    // ver todos
    @GetMapping
    public ResponseEntity<List<Autor>> listar() {
        List<Autor> autores = autorService.findAll();
        if(autores.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

    //crear
    @PostMapping
    public ResponseEntity<Autor> 
        guardar(@RequestBody Autor autor){
            Autor nAutor = 
                autorService.save(autor);
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(nAutor);
    }

    //borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
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
        List<Autor> autores = autorService.findByApPaterno(apellido);
        if (autores.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(autores);
    }

}
