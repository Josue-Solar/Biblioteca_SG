package com.biblioteca.reserva.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.reserva.model.Reserva;
import com.biblioteca.reserva.repository.ReservaRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepo;

    public List<Reserva> getReservas(){
        return reservaRepo.findAll();
    }

    public Optional<Reserva> getResByID(long id){
        return reservaRepo.findById(id);
    }

    public Reserva addReserva(Reserva res){
        return reservaRepo.save(res);
    }

    public Optional<Reserva> modReserva(long id, Reserva nRes){
        return reservaRepo.findById(id).map(r -> {
            r.setPersonaId(nRes.getPersonaId());
            r.setEjemplarId(nRes.getEjemplarId());
            return reservaRepo.save(r);
        });
    }

    public void deleteByID(long id){
        reservaRepo.deleteById(id);
    }

    //get by personaID
    //get by ejemplarID

}
