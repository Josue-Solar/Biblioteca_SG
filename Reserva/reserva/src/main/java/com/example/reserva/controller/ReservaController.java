package com.example.reserva.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;

import com.example.reserva.model.Reserva;
import com.example.reserva.service.ReservaService;

@RestController
@RequestMapping("/api/v1/reserva")
public class ReservaController {

    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class.getName());

    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> listar(){
        List<Reserva> res = reservaService.findAll();
        if(res.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(res);
    }

    @GetMapping("id:{id}")
    public ResponseEntity<Reserva> getByID(@PathVariable long id){
        try {
            Reserva res = reservaService.findByID(id);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("personaID:{id}")
    public ResponseEntity<Reserva> getByPersonaID(@PathVariable long id){
        try {
            Reserva res = reservaService.findByPersonaID(id);
            return ResponseEntity.ok(res);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Reserva> registrar(@RequestBody Reserva res){
        Reserva nReserva = reservaService.guardar(res);
        return ResponseEntity.status(HttpStatus.CREATED).body(nReserva);
    } 

    @DeleteMapping("/eliminar:{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id){
        try{
            reservaService.borrarById(id);
            return ResponseEntity.noContent().build();
        }catch(Exception ex){
            return ResponseEntity.notFound().build();
        }
    }
}
