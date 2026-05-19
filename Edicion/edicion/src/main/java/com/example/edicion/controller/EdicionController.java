package com.example.edicion.controller;

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

import com.example.edicion.dto.EdicionDTO;
import com.example.edicion.service.EdicionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/ediciones")
@RequiredArgsConstructor
public class EdicionController {


    private final EdicionService edicionService;

    @GetMapping
    public List<EdicionDTO.Response> getAllEdics(){
        return edicionService.obtenerTodos();
    }


    @GetMapping("/librosPorEdicion/{edicionId}")
    public ResponseEntity<?> librosPorEdicion(@PathVariable Long edicionId){
        return ResponseEntity.ok(edicionService.librosPorEdicion(edicionId));
    }

    @GetMapping("/id:{id}")
    public ResponseEntity<?> getByID(@Valid @PathVariable long id){
        try{
            return ResponseEntity.ok(edicionService.obtenerPorId(id));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/edicionesPorEditorial:{id}")
    public ResponseEntity<?> getAutoresPorLibro(@PathVariable Long id){
        try{
            return ResponseEntity.ok(edicionService.listarEdiciones(id));
        }catch(Exception ex){
            ex.printStackTrace(); 
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping("/editorialId/{id}")
    public ResponseEntity<?> getAllByEditorialId(@PathVariable Long id){
        return ResponseEntity.ok(edicionService.listarEdiciones(id));
    }

    @PostMapping
    public ResponseEntity<EdicionDTO.Response> addLibro(@Valid @RequestBody EdicionDTO.Request edic){
        return ResponseEntity.status(HttpStatus.CREATED).body(edicionService.guardar(edic));
    }

    @PutMapping("/editar:{id}")
    public ResponseEntity<EdicionDTO.Response> putEdicion(@Valid @RequestBody EdicionDTO.Request request, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(edicionService.actualizar(id, request));
    }

    @DeleteMapping("/eliminar:{id}")
    public ResponseEntity<Boolean> deleteEdicion(@Valid @PathVariable Long id){
        return edicionService.eliminar(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }



}
