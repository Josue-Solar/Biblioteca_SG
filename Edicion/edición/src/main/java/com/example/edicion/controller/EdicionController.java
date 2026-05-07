package com.example.edicion.controller;

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

import com.example.edicion.model.Edicion;
import com.example.edicion.service.EdicionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v2/ediciones")
public class EdicionController {

    private static final Logger logger = LoggerFactory.getLogger(EdicionController.class.getName());
    
    @Autowired
    private EdicionService edicionService;

    @GetMapping
    public ResponseEntity<Object> listar(){
        logger.info("Recibiendo solicitud para listar ediciones");//log
        List<Edicion> ediciones = edicionService.obtenerTodos();
        if(ediciones.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ediciones);
    }

    @GetMapping("/nombre/{nombre}") //buscar por apellido
    public ResponseEntity<List<Edicion>> buscarPorNombre(@PathVariable String nombre) {
        logger.info("Recibiendo solicitud para buscar edicion por nombre: " + nombre);//log
        List<Edicion> ediciones = edicionService.obtenerPorNombre(nombre);
        if (ediciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ediciones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Edicion> buscarPorId(@PathVariable long id) {
        logger.info("Recibiendo solicitud para buscar edicion por ID: " + id);//log
        try {
            Edicion edicion = edicionService.findByIdOrThrow(id);
            return ResponseEntity.ok(edicion);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
    

    @PostMapping
    public ResponseEntity<Edicion> crear(@RequestBody Edicion edicion) {
        logger.info("Recibiendo solicitud para crear edicion");//log
        Edicion nuevaEdicion = edicionService.guardar(edicion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEdicion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Edicion> actualizar(@PathVariable long id, @Valid @RequestBody Edicion edicion) {
        logger.info("Recibiendo solicitud para actualizar edicion" + id);
        Edicion edicionActualizada = edicionService.modificarEdicion(id, edicion);  
        if (edicionActualizada != null) {
            return ResponseEntity.ok(edicionActualizada);
        }
        return ResponseEntity.notFound().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable long id) {
        logger.info("Recibiendo solicitud para eliminar edicion por ID: " + id);//log
        try {
            edicionService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



}
