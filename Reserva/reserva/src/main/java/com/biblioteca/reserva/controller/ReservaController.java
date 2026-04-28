package com.biblioteca.reserva.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import com.biblioteca.reserva.model.Reserva;
import com.biblioteca.reserva.service.ReservaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reservas")
@RequiredArgsConstructor
public class ReservaController {

    private final ReservaService reservaService;
    private static final Logger logger = LoggerFactory.getLogger(ReservaController.class.getName());


    @GetMapping
    public List<Reserva> getAll(){
        return reservaService.getReservas();
    }

    @GetMapping("/id:{id}")
    public ResponseEntity<Reserva> getByID(@PathVariable long id){
        return reservaService.getResByID(id).map(ResponseEntity::ok)
                                            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reserva> postReserva(@Valid @RequestBody Reserva res){
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.addReserva(res));
    }

    @PutMapping("/editar:{id}")
    public ResponseEntity<Reserva> putReserva(@Valid @RequestBody Reserva res, @PathVariable long id){
        return reservaService.modReserva(id, res).map(ResponseEntity::ok)
                                                 .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/eliminar:{id}")
    public ResponseEntity<Void> deleteByID(@PathVariable long id){
        reservaService.deleteByID(id);
        return ResponseEntity.noContent().build();
    }
    
}
