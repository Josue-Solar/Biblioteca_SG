package com.example.reserva.service;

import com.example.reserva.client.PersonaClient;
import com.example.reserva.model.Reserva;
import com.example.reserva.repository.ReservaRepository;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepo;

    private final PersonaClient personaClient;

    public List<Reserva> findAll(){
        return reservaRepo.findAll();
    }

    public Reserva findByID(long id){
        return reservaRepo.findById(id).orElseThrow(() -> new RuntimeException("Persona con id "+ id + " no encontrada"));
    }

    public String findByPersonaID(long id){
        try {
            personaClient.buscarPorId(id);
            log.info(">>> Especialidad {} validada correctamente (FeignClient)", id);
            return personaClient.buscarPorId(id);

        } catch (FeignException.NotFound e) {
            throw new RuntimeException(
                    "La especialidad con id " + id + " no existe en ms-especialidades.");
        } catch (FeignException e) {
            throw new RuntimeException(
                    "No se puede conectar con ms-especialidades: " + e.getMessage());
        }
    }

    public Reserva guardar(Reserva res){
        return reservaRepo.save(res);
    }

    public void borrarById(long id){
        reservaRepo.deleteById(id);
    }
}
