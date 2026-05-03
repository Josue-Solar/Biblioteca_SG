package com.biblioteca.reserva.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.biblioteca.reserva.client.PersonaClient;
import com.biblioteca.reserva.dto.PersonaDTO;
import com.biblioteca.reserva.dto.ReservaPersonaDTO;
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
            r.setPersonaID(nRes.getPersonaID());
            r.setEjemplarID(nRes.getEjemplarID());
            return reservaRepo.save(r);
        });
    }

    public void deleteByID(long id){
        reservaRepo.deleteById(id);
        
    }

    private final PersonaClient personaClient;
    //get by personaID
    public ReservaPersonaDTO getReservaDatosPersona(long id){
        /*return reservaRepo.findAll().stream().map(reserva -> {
            PersonaDTO persona = personaClient.buscarPorId(id);
            return new ReservaPersonaDTO(
                reserva.getId(),
                persona.getId(),
                persona.getNombreCompleto()
            );
        })
        .collect(Collectors.toList());*/
        PersonaDTO persona = personaClient.buscarPorId(id);
        ReservaPersonaDTO resPersonaDTO = new ReservaPersonaDTO(persona, reservaRepo.findByPersonaID(id));
        return resPersonaDTO;   
    }
    //get by ejemplarID
    public List<Reserva> getByEjID(long id){
        return reservaRepo.findByEjemplarID(id);
    }
}
