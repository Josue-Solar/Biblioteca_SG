package com.example.reserva.service;

import com.example.reserva.model.Reserva;
import com.example.reserva.repository.ReservaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepo;

    private final WebClient webClient;

    public List<Reserva> findAll(){
        return reservaRepo.findAll();
    }

    public Reserva findByID(Long id){
        return reservaRepo.findById(id).orElseThrow(() -> new RuntimeException("Persona con id "+ id + " no encontrada"));
    }

    public List<Reserva> findByPersonaID(Long id){
        return reservaRepo.findByPersonaId(id);
    }

    public Reserva guardar(Reserva res){
        return reservaRepo.save(res);
    }

    public void borrarById(Long id){
        reservaRepo.deleteById(id);
    }
}
